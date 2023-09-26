package com.qxdzbc.p6.composite_actions.cell.copy_cell

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.composite_actions.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.composite_actions.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.composite_actions.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.command.BaseCommand
import com.qxdzbc.p6.command.Command
import com.qxdzbc.p6.document_data_layer.cell.CellContent
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.cell.CellImp
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.format.FormatConfig
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class CopyCellActionImp @Inject constructor(
    val stateCont:StateContainer,
    val updateCellFormatAction: UpdateCellFormatAction,
    val updateCellAction: UpdateCellAction,
    val errorRouter: ErrorRouter,
) : CopyCellAction {

    private val sc  = stateCont

    override fun copyCellWithoutClipboard(request: CopyCellRequest,publishError:Boolean): Rse<Unit> {

        if (request.undoable) {
            val command = makeCopyCommand(request)
            sc.getUndoStackMs(request.toCell)?.also {
                it.value = it.value.add(command)
            }
        }
//        copyData(request)
//        copyFormat(request)
//        return Ok(Unit)
        val rs1 = copyData(request)
        val rs2 = copyFormat(request)
        if(publishError){
            rs1.onFailure { errorReport ->
                errorRouter.publishToWindow(errorReport,request.toCell.wbKey)
            }
            rs2.onFailure { errorReport ->
                errorRouter.publishToWindow(errorReport,request.toCell.wbKey)
            }
        }
        if(rs1 is Ok && rs2 is Ok){
            return Ok(Unit)
        }
        if(rs1 is Err){
            if(rs2 is Err){
                return CommonErrors.MultipleErrors.report(listOf(rs1.error,rs2.error)).toErr()
            }else{
                return rs1
            }
        }
        if(rs2 is Err){
            if(rs1 is Err){
                return CommonErrors.MultipleErrors.report(listOf(rs1.error,rs2.error)).toErr()
            }else{
                return rs2
            }
        }
        return CommonErrors.Unknown.report("unknown error in CopyCellActionImp").toErr()
    }

    fun makeCopyCommand(request: CopyCellRequest): Command {
        val rt = object : BaseCommand() {
            val _request = request
            val _toCellId: CellId? get() = sc.getCellId(_request.toCell)
            var oldCellDM: CellDM? = null
            var oldCellFormat: FormatConfig? = null
            init{
                storeOldData()
            }
            private fun storeOldData(){
                oldCellDM = _toCellId?.let { sc.getCellMs(it)?.value?.toDm() }
                oldCellFormat = sc.getCellFormatTable(request.toCell)
                    ?.getFormatConfigForCells(request.toCell.address)
            }

            override fun run() {
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
            val toCellMs: Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>? = sc.getCellMs(request.toCell)
            val rs3: Rse<Unit> = sc.getWsMsRs(request.toCell).flatMap { toWsMs ->
                val toWs: Worksheet by toWsMs
                val wsState: WorksheetState? = sc.getWsState(request.toCell)
                // x: create the new cell if the destination cell does not exist
                val newToWs: Worksheet = if (toCellMs == null) {
                    val newCell = CellImp(
                        id = CellId(request.toCell.address, toWs.wbKeySt, toWs.wsNameSt)
                    )
                    // x: add the new cell to the worksheet
                    toWsMs.value.addOrOverwrite(newCell)
                    toWsMs.value
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
                        wsState?.refresh()
                        Ok(Unit)
                    }
                // x: reRun the workbook containing the destination cell
                val rs2 = rs0.flatMap {
                    sc.getWbMsRs(toWs.wbKeySt).flatMap {
                        it.value.reRun()
                        Ok(Unit)
                    }
                }
                rs2
            }
            rs3
        }
        return rt
    }

    private fun copyFormat(request: CopyCellRequest):Rse<Unit> {
        val fromCell = request.fromCell
        val formatRs = sc.getCellFormatTableRs(fromCell).map {
            it.getFormat(fromCell.address)
        }
        val q = formatRs.flatMap {format->
            val toCell = request.toCell
            sc.getCellFormatTableMsRs(toCell).map {
                it.value = it.value.setFormat(toCell.address, format)
            }
        }
        return q
//        val format = sc.getCellFormatTable(fromCell)?.getFormat(fromCell.address)
//        if (format != null) {
//            val toCell = request.toCell
//            sc.getCellFormatTableMs(toCell)?.also {
//                it.value = it.value.setFormat(toCell.address, format)
//            }
//        }

    }
}
