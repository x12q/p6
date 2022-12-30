package com.qxdzbc.p6.app.action.worksheet.paste_range

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeIdDM
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellRequest
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.copy_paste.RangePaster
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class PasteRangeActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val paster: RangePaster,
    private val wbUpdateApplier: WorkbookUpdateCommonApplier,
    val docContMs: Ms<DocumentContainer>,
    val updateCellFormatAction: UpdateCellFormatAction,
    val updateCellAction: UpdateCellAction,
    val updateMultiCellAction: UpdateMultiCellAction,
    val deleteMultiCellAction: DeleteMultiCellAction,
) : PasteRangeAction {

    private val sc by stateContSt
    private val dc by docContMs

    override fun pasteRange(wbwsSt: WbWsSt, rangeAddress: RangeAddress, undo: Boolean): Rse<Unit> {
        val wbkSt = wbwsSt.wbKeySt
        val wsNameSt = wbwsSt.wsNameSt
        val windowId = sc.getWindowStateMsByWbKey(wbwsSt.wbKey)?.value?.id
        val wsState = sc.getWsState(wbwsSt)

        if (wsState != null) {
            val targetRangeId = RangeIdImp(
                rangeAddress = rangeAddress,
                wbKeySt = wsState.wbKeySt,
                wsNameSt = wsState.wsNameSt
            )
            val clipboardData: RangeCopy? = paster.readRangeCopyFromClipboard(wbwsSt.wbKey,wbwsSt.wsName)
            val shiftedClipboardData = clipboardData?.shiftCells(targetRangeId.rangeAddress.topLeft)
            shiftedClipboardData?.also {
                val command = object : BaseCommand() {
                    val _clipboardData = shiftedClipboardData!!
                    val sourceRangeId = _clipboardData.rangeId
                    val _targetRangeId = targetRangeId
                    val oldData = RangeCopyDM.findRangeCopyInAppState(RangeIdDM(
                        rangeAddress = rangeAddress,
                        wbKey = wsState.wbKey,
                        wsName = wsState.wsName
                    ),sc)

                    val sourceFormat = sourceRangeId
                        .let { sc.getCellFormatTable(it) }
                        ?.getFormatConfigForRange(sourceRangeId.rangeAddress)
                        ?.shift(sourceRangeId.rangeAddress.topLeft,_targetRangeId.rangeAddress.topLeft)
                    val targetFormat = sc.getCellFormatTable(_targetRangeId)
                        ?.getFormatConfigForRange(_targetRangeId.rangeAddress)
                    override fun run() {
                        // x: write clipboard data to target
                        updateMultiCellAction.updateMultiCell(
                            request=  UpdateMultiCellRequest(
                                wbKeySt = wbwsSt.wbKeySt,
                                wsNameSt = wbwsSt.wsNameSt,
                                cellUpdateList = _clipboardData.cells.map{ IndCellDM(it.address,it.content.toDm()) }
                            )
                        )

                        // x: apply cell format to target
                        targetFormat?.also {
                            updateCellFormatAction.clearFormat_Respective(_targetRangeId, targetFormat, false)
                        }
                        sourceFormat?.also {
                            updateCellFormatAction.applyFormatConfig(_targetRangeId, sourceFormat, false)
                        }
                    }

                    override fun undo() {
                        // x: undo cell data = delete the clipboard data + restore the old data

                        deleteMultiCellAction.deleteMultiCell(
                            request = RemoveMultiCellRequest(
                                ranges=listOf(_clipboardData.rangeId.rangeAddress.shift(
                                    sourceRangeId.rangeAddress.topLeft,_targetRangeId.rangeAddress.topLeft
                                )),
                                wbKey = wbwsSt.wbKey,
                                wsName =  wbwsSt.wsName,
                                clearFormat =false,
                                windowId = null
                            ),
                            undoable=false,
                        )

                        oldData?.also {
                            updateMultiCellAction.updateMultiCellDM(
                                request= MultiCellUpdateRequestDM(
                                    wsId = WorksheetIdDM(oldData.wbKey,oldData.wsName),
                                    cellUpdateList = oldData.cells.map{ c->c.toIndCellDM() }
                                )
                            )
                        }

                        // x: undo cell format
                        sourceFormat?.also {
                            updateCellFormatAction.clearFormat_Respective(targetRangeId, sourceFormat, false)
                        }
                        targetFormat?.also {
                            updateCellFormatAction.applyFormatConfig(targetRangeId, targetFormat, false)
                        }
                    }
                }

                if (undo) {
                    val commandStackMs = sc.getCommandStackMs(wbwsSt)
                    if (commandStackMs != null) {
                        commandStackMs.value = commandStackMs.value.add(command)
                    }
                }
                command.run()
            }
        }

        return Ok(Unit)
    }

    override fun pasteRange(wbws: WbWs, rangeAddress: RangeAddress, undo: Boolean): Rse<Unit> {
        val rt = sc.getWbWsStRs(wbws).flatMap {
            this.pasteRange(it, rangeAddress, undo)
        }
        return rt
    }
}
