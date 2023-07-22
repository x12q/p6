package com.qxdzbc.p6.app.action.worksheet.paste_range

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequestDM
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellRequest
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.copy_paste.ClipboardReader
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.format.FormatConfig
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class PasteRangeActionImp @Inject constructor(
    private val stateCont:StateContainer,
    val updateCellFormatAction: UpdateCellFormatAction,
    val updateMultiCellAction: UpdateMultiCellAction,
    val deleteMultiCellAction: DeleteMultiCellAction,
    val clipboardReader: ClipboardReader,
) : PasteRangeAction {

    private val sc  = stateCont

    override fun pasteRange(targetWbWsSt: WbWsSt, targetRangeAddress: RangeAddress, undoable: Boolean): Rse<Unit> {
        val targetRangeId = RangeIdImp(
            rangeAddress = targetRangeAddress,
            wbKeySt = targetWbWsSt.wbKeySt,
            wsNameSt = targetWbWsSt.wsNameSt
        )
        val clipboardData: RangeCopy? = clipboardReader.readDataFromClipboard(targetWbWsSt.wbKey, targetWbWsSt.wsName)
        val shiftedClipboardData = clipboardData?.shiftCells(targetRangeId.rangeAddress.topLeft)
        shiftedClipboardData?.also {
            val command = makePasteCommand(targetRangeId,shiftedClipboardData)
            command?.also {
                if (undoable) {
                    val commandStackMs = sc.getUndoStackMs(targetWbWsSt)
                    if (commandStackMs != null) {
                        commandStackMs.value = commandStackMs.value.add(command)
                    }
                }
                command.run()
            }
        }
        return Ok(Unit)
    }

    fun makePasteCommand(
        targetRangeId:RangeId,
        shiftedClipboardData: RangeCopy,
    ): Command? {

        val rt = sc.getWbWsSt(targetRangeId)?.let {targetWbWsSt->
            val command = object : BaseCommand() {
                val _originalTargetRangeId = targetRangeId
                val _targetWbWsSt = targetWbWsSt
                /*
                _targetRangeId is recreated each time it is used to ensure that it contains the latest wb key and ws name
                 */
                val _targetRangeId get()= RangeIdImp(
                    _originalTargetRangeId.rangeAddress,_targetWbWsSt.wbKeySt,_targetWbWsSt.wsNameSt
                )

                val _shiftedClipboardData = shiftedClipboardData
                val _sourceRangeId = _shiftedClipboardData.rangeId
                val _oldData = RangeCopyDM.findRangeCopyInAppState(
                    _targetRangeId, sc
                )

                val _shiftedSourceFormat = _sourceRangeId
                    .let { sc.getCellFormatTable(it) }
                    ?.getFormatConfigForRange(_sourceRangeId.rangeAddress)
                    ?.shift(_sourceRangeId.rangeAddress.topLeft, targetRangeId.rangeAddress.topLeft)

                val _targetFormat = sc.getCellFormatTable(targetRangeId)
                    ?.getFormatConfigForRange(targetRangeId.rangeAddress)

                override fun run() {
                    paste(
                        _targetRangeId, _shiftedClipboardData, _targetFormat, _shiftedSourceFormat
                    )
                }

                override fun undo() {
                    /*
                    restore data + format of target range
                     */

                    // x: undo data = delete the data written from clipboard data + write back the old data
                    deleteMultiCellAction.deleteDataOfMultiCell(
                        request = DeleteMultiCellRequest(
                            ranges = listOf(
                                _shiftedClipboardData.rangeId.rangeAddress.shift(
                                    _sourceRangeId.rangeAddress.topLeft, targetRangeId.rangeAddress.topLeft
                                )
                            ),
                            wbKey = _targetRangeId.wbKey,
                            wsName = _targetRangeId.wsName,
                            clearFormat = false,
                            windowId = null
                        ),
                        undoable = false,
                    )

                    _oldData?.also {
                        updateMultiCellAction.updateMultiCellDM(
                            request = UpdateMultiCellRequestDM(
                                wsId = WorksheetIdDM(_targetWbWsSt.wbKey, _targetWbWsSt.wsName),
                                cellUpdateList = _oldData.cells.map { c -> c.toIndCellDM() }
                            )
                        )
                    }

                    // x: undo cell format = remove the current format + write back the old format
                    _shiftedSourceFormat?.also {
                        updateCellFormatAction.clearFormat_Respective(_targetRangeId, _shiftedSourceFormat, false)
                    }
                    _targetFormat?.also {
                        updateCellFormatAction.applyFormatConfig(_targetRangeId, _targetFormat, false)
                    }
                }
            }
            command
        }
        return rt
    }

    /**
     * paste [shiftedClipboardData], [shiftedSourceFormat] into range at [targetWbWsSt], [targetRangeId].
     * use [targetFormat] to clear the target format before pasting format data
     * @param shiftedClipboardData is a [RangeCopy] that has its cells' addresses shifted using the vector [source range's top left -> target range's top left]
     * @param shiftedSourceFormat is a [FormatConfig] that has its entries' range addresses shifted using the vector [source range's top left -> target range's top left]
     */
    fun paste(
        targetRangeId: RangeId,
        shiftedClipboardData: RangeCopy,
        targetFormat: FormatConfig?,
        shiftedSourceFormat: FormatConfig?,
    ) {
        sc.getWbWsSt(targetRangeId)?.also { targetWbWsSt ->
            updateMultiCellAction.updateMultiCell(
                request = UpdateMultiCellRequest(
                    wbKeySt = targetWbWsSt.wbKeySt,
                    wsNameSt = targetWbWsSt.wsNameSt,
                    cellUpdateList = shiftedClipboardData.cells.map { IndependentCellDM(it.address, it.content.toDm()) }
                )
            )

            // x: apply cell format to target
            targetFormat?.also {
                updateCellFormatAction.clearFormat_Respective(targetRangeId, targetFormat, false)
            }
            shiftedSourceFormat?.also {
                updateCellFormatAction.applyFormatConfig(targetRangeId, shiftedSourceFormat, false)
            }
        }
    }

    override fun pasteRange(wbws: WbWs, rangeAddress: RangeAddress, undo: Boolean): Rse<Unit> {
        val rt = sc.getWbWsStRs(wbws).flatMap {
            this.pasteRange(it, rangeAddress, undo)
        }
        return rt
    }
}
