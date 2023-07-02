package com.qxdzbc.p6.app.action.worksheet.delete_multi.rm

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAtCursorRequest
import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellResponse
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellRequest
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteMultiRMImp @Inject constructor(
    val stateCont:StateContainer,
) : DeleteMultiRM {

    override fun deleteMultiCellAtCursor(request: DeleteMultiCellAtCursorRequest): RseNav<RemoveMultiCellResponse> {
        val rangeCells = stateCont.getWsStateRs(request.wbKey, request.wsName).map { wsState ->
            val ranges = wsState.cursorState.allRanges
            val cells = wsState.cursorState.allFragCells
            (ranges to cells)
        }
        val ranges = rangeCells.component1()?.first ?: emptyList()
        val cells = rangeCells.component1()?.second ?: emptyList()
        val q = deleteMultiCell(
            DeleteMultiCellRequest(
                ranges = ranges,
                cells = cells,
                wbKey = request.wbKey,
                wsName = request.wsName,
                clearFormat = request.clearFormat,
                windowId = request.windowId
            )
        )
        return q
    }

    override fun deleteMultiCell(request: DeleteMultiCellRequest): RseNav<RemoveMultiCellResponse> {
        val wbk = request.wbKey
        val wsn = request.wsName
        val rt = stateCont.getWbRs(wbk).flatMap { wb ->
            wb.getWsRs(wsn).flatMap { ws ->
                val ranges: List<RangeAddress> = request.ranges
                val cells: List<CellAddress> = request.cells
                // x: remove cell from worksheet
                var newWs = ws.removeCells(cells)
                val cellsInRanges = ranges
                    .flatMap { newWs.getCellsInRange(it) }
                    .map { it.address }
                    .toSet()
                newWs = newWs.removeCells(cellsInRanges)
                val newWb = wb.addSheetOrOverwrite(newWs).reRun()
                val oldWsState = stateCont.getWsState(wbk, wsn)
                val newWsState = if (request.clearFormat) {
                    stateCont.getWsState(wbk, wsn)?.removeCellState(cells + cellsInRanges)
                } else {
                    oldWsState
                }
                Ok(
                    RemoveMultiCellResponse(
                        newWb = newWb,
                        newWsState = newWsState?.refreshCellState()
                    )
                )
            }
        }
        return rt.mapError { it.withNav(request) }
    }
}
