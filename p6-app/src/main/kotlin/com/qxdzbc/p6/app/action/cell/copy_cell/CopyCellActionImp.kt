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

    override fun copyCell(request: CopyCellRequest): Rse<Unit> {
        val rt:Rse<Unit> = sc.getCellMsRs(request.fromCell).flatMap { fromCellMs ->
            val toCellMs: Ms<Cell>? = sc.getCellMs(request.toCell)
            val q2: Rse<Unit> = sc.getWsMsRs(request.toCell).flatMap { wsMs ->
                // x: get the worksheet
                val ws:Worksheet by wsMs
                val wsStateMs: Ms<WorksheetState>? = sc.getWsStateMs(request.toCell)
                // x: create the new cell if the destination cell does not exist
                val newWs: Worksheet = if (toCellMs == null) {
                    val newCell = CellImp(
                        id = CellId(request.toCell.address, ws.wbKeySt, ws.wsNameSt)
                    )
                    // x: add the new cell to the worksheet
                    val newWs: Worksheet = wsMs.value.addOrOverwrite(newCell)
                    wsMs.value = newWs
                    newWs
                } else {
                    wsMs.value
                }

                // x: update the destination cell
                val q0 = newWs.getCellMsRs(request.toCell.address)
                    .flatMap { newCellMs ->
                        val copyContent: CellContent = if (request.shiftRange) {
                            fromCellMs.value.content.shift(
                                oldAnchorCell = fromCellMs.value.address,
                                newAnchorCell = newCellMs.value.address
                            )
                        } else {
                            fromCellMs.value.content
                        }
                        newCellMs.value = newCellMs.value.setContent(copyContent)
                        wsStateMs?.let {
                            it.value = it.value.refresh()
                        }
                        Ok(Unit)
                    }
                // x: reRun the workbook containing the destination cell
                val q1 = q0.flatMap {
                    sc.getWbMsRs(ws.wbKeySt).flatMap {
                        it.value = it.value.reRun()
                        Ok(Unit)
                    }
                }
                q1
            }
            q2
        }
        return rt
    }
}
