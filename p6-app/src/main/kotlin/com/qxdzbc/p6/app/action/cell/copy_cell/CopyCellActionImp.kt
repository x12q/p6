package com.qxdzbc.p6.app.action.cell.copy_cell

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellContent
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.CellImp
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.format.FormatConfig
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CopyCellActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    val deleteMultiCellAction: DeleteMultiCellAction,
    val updateCellFormatAction: UpdateCellFormatAction,
    val updateMultiCellAction: UpdateMultiCellAction,
    val updateCellAction: UpdateCellAction,
) : CopyCellAction {

    private val sc by stateContSt

    override fun copyCellWithoutClipboard(request: CopyCellRequest): Rse<Unit> {
        if (request.undoable) {
            sc.getUndoStackMs(request.toCell)?.also {
                val command = makeCopyCommand(request)
                it.value = it.value.add(command)
            }
        }

        val rt = copyData(request)
        copyFormat(request)
        return rt
    }

    fun makeCopyCommand(request: CopyCellRequest): Command {
        val rt = object : BaseCommand() {
            val _request = request
            val _toCellId: CellId? get() = sc.getCellId(_request.toCell)
            var oldCellDM: CellDM? = null
            var oldCellFormat: FormatConfig? = null

            private fun storeOldData(){
                oldCellDM = _toCellId?.let { sc.getCellMs(it)?.value?.toDm() }
                oldCellFormat = sc.getCellFormatTable(request.toCell)
                    ?.getFormatConfigForCells(request.toCell.address)
            }

            override fun run() {
                storeOldData()
                copyData(request)
                copyFormat(request)
            }

            override fun undo() {
                _toCellId?.let { oldCellId ->
                    //x: write back old cell value
                    oldCellDM?.content?.also { oldCellContentDM ->
                        updateCellAction.updateCell(
                            request = CellUpdateRequest(
                                cellId = oldCellId,
                                cellContentDM = oldCellContentDM
                            ),
                        )
                    }
                    //x: write back old cell format
                    oldCellFormat?.also {
                        updateCellFormatAction.applyFormatConfig(
                            wbWsSt = oldCellId,
                            config= it,
                            undoable = false,
                        )
                    }
                }
            }
        }
        return rt
    }

    private fun copyData(request: CopyCellRequest): Rse<Unit> {
        val rt: Rse<Unit> = sc.getCellMsRs(request.fromCell).flatMap { fromCellMs ->
            val toCellMs: Ms<Cell>? = sc.getCellMs(request.toCell)
            val rs3: Rse<Unit> = sc.getWsMsRs(request.toCell).flatMap { toWsMs ->
                val toWs: Worksheet by toWsMs
                val wsStateMs: Ms<WorksheetState>? = sc.getWsStateMs(request.toCell)
                // x: create the new cell if the destination cell does not exist
                val newToWs: Worksheet = if (toCellMs == null) {
                    val newCell = CellImp(
                        id = CellId(request.toCell.address, toWs.wbKeySt, toWs.wsNameSt)
                    )
                    // x: add the new cell to the worksheet
                    val newWs: Worksheet = toWsMs.value.addOrOverwrite(newCell)
                    toWsMs.value = newWs
                    newWs
                } else {
                    toWsMs.value
                }

                // x: update the destination cell
                val rs0 = newToWs.getCellMsRs(request.toCell.address)
                    .flatMap { toCellMs ->
                        val targetContent: CellContent = if (request.shiftRange) {
                            fromCellMs.value.content.shift(
                                oldAnchorCell = fromCellMs.value.address,
                                newAnchorCell = toCellMs.value.address
                            )
                        } else {
                            fromCellMs.value.content
                        }
                        toCellMs.value = toCellMs.value.setContent(targetContent)
                        wsStateMs?.let {
                            it.value = it.value.refresh()
                        }
                        Ok(Unit)
                    }
                // x: reRun the workbook containing the destination cell
                val rs2 = rs0.flatMap {
                    sc.getWbMsRs(toWs.wbKeySt).flatMap {
                        it.value = it.value.reRun()
                        Ok(Unit)
                    }
                }
                rs2
            }
            rs3
        }
        return rt
    }

    private fun copyFormat(request: CopyCellRequest) {
        val fromCell = request.fromCell
        val format = sc.getCellFormatTable(fromCell)?.getFormat(fromCell.address)
        if (format != null) {
            val toCell = request.toCell
            sc.getCellFormatTableMs(toCell)?.also {
                it.value = it.value.setFormat(toCell.address, format)
            }
        }
    }
}
