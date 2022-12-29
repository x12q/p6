package com.qxdzbc.p6.app.action.cell.copy_cell

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellContent
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.CellImp
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CopyCellActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : CopyCellAction {

    private val sc by stateContSt

    override fun copyCellWithoutClipboard(request: CopyCellRequest): Rse<Unit> {
        val rt = copyData(request)
        copyFormat(request)
        return rt
    }

    private fun copyData(request: CopyCellRequest):Rse<Unit>{
        val rt:Rse<Unit> = sc.getCellMsRs(request.fromCell).flatMap { fromCellMs ->
            val toCellMs: Ms<Cell>? = sc.getCellMs(request.toCell)
            val rs3: Rse<Unit> = sc.getWsMsRs(request.toCell).flatMap { toWsMs ->
                val toWs:Worksheet by toWsMs
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

    private fun copyFormat(request: CopyCellRequest){
        val fromCell = request.fromCell
        val format = sc.getCellFormatTable(fromCell)?.getFormat(fromCell.address)
        if(format!=null){
            val toCell = request.toCell
            sc.getCellFormatTableMs(toCell)?.also {
                it.value =it.value.setFormat(toCell.address,format)
            }
        }
    }
}
