package com.emeraldblast.p6.app.action.worksheet.delete_multi.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiRequest2
import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiResponse2
import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.emeraldblast.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.DeleteMultiRequest
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.DeleteMultiResponse
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.mapError
import javax.inject.Inject

class DeleteMultiRMImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>
) : DeleteMultiRM {
    var appState by appStateMs

    override fun deleteMulti2(request: DeleteMultiRequest2): RseNav<DeleteMultiResponse2> {
        val wbk = request.wbKey
        val wsn = request.wsName

        val rt = appState.getWorkbookRs(wbk).flatMap { wb ->
            wb.getWsRs(wsn).flatMap { ws ->
                appState.getWsStateRs(wbk, wsn).flatMap { wsState ->
                    val ranges = wsState.cursorState.allRanges
                    val cells = wsState.cursorState.allFragCells
                    // remove cell from worksheet
                    var newWs = ws.removeCells(cells)
                    val cellsInRanges = ranges
                        .flatMap { newWs.getCellsInRange(it) }
                        .map { it.address }
                        .toSet()
                    newWs = newWs.removeCells(cellsInRanges)
                    val newWb = wb.addSheetOrOverwrite(newWs).reRun()
                    val oldWsState=appState.getWsState(wbk, wsn)
                    val newWsState = if (request.clearFormat) {
                        appState.getWsState(wbk, wsn)?.removeCellState(cells + cellsInRanges)
                    } else {
                        oldWsState
                    }
                    Ok(
                        DeleteMultiResponse2(
                            newWb = newWb,
                            newWsState = newWsState?.refreshCellState()
                        )
                    )
                }
            }
        }
        return rt.mapError { it.withNav(request) }
    }

    override fun deleteMulti(request: DeleteMultiRequest): DeleteMultiResponse? {
        val wbk = request.wbKey
        val wbRs = appState.getWorkbookRs(wbk)
        val rt = wbRs.mapBoth(
            success = { wb ->
                val wsRs = wb.getWsRs(request.wsName)
                wsRs.mapBoth(
                    success = { ws ->
                        var newWs = ws
                        for (address in request.cells) {
                            newWs = newWs.removeCell(address)
                        }
                        val targetCells = request.ranges
                            .flatMap { newWs.getCellsInRange(it) }
                            .map { it.address }
                            .toSet()
                        for (cellAddress in targetCells) {
                            newWs = newWs.removeCell(cellAddress)
                        }
                        val newWb = wb.addSheetOrOverwrite(newWs)
                        DeleteMultiResponse(
                            WorkbookUpdateCommonResponse(
                                wbKey = wbk,
                                newWorkbook = newWb,
                                errorReport = null
                            )
                        )
                    },
                    failure = { wsErr ->
                        DeleteMultiResponse(
                            WorkbookUpdateCommonResponse(
                                wbKey = wbk,
                                newWorkbook = null,
                                errorReport = wsErr
                            )
                        )
                    }
                )
            },
            failure = {
                DeleteMultiResponse(
                    WorkbookUpdateCommonResponse(
                        wbKey = wbk,
                        newWorkbook = null,
                        errorReport = it
                    )
                )
            }
        )
        return rt
    }

}
