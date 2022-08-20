package com.emeraldblast.p6.app.action.worksheet.delete_cell.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.worksheet.delete_cell.DeleteCellRequest
import com.emeraldblast.p6.app.action.worksheet.delete_cell.DeleteCellResponse
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.mapBoth
import javax.inject.Inject

class DeleteCellRMImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>
) : DeleteCellRM {
    var appState by appStateMs
    override fun deleteCell(request: DeleteCellRequest): DeleteCellResponse {
        val wbk = request.wbKey
        val wbRs = appState.getWbRs(wbk)
        val rt = wbRs.mapBoth(
            success = { wb ->
                val wsRs = wb.getWsRs(request.wsName)
                wsRs.mapBoth(
                    success = { ws ->
                        val newWs = ws.removeCell(request.cellAddress)
                        val newWb = wb.addSheetOrOverwrite(newWs)
                        DeleteCellResponse(
                            wbKey = wbk,
                            wsName = request.wsName,
                            cellAddress = request.cellAddress,
                            newWorkbook = newWb,
                            isError = false,
                            errorReport = null
                        )
                    },
                    failure = { wser ->
                        DeleteCellResponse(
                            wbKey = wbk,
                            wsName = request.wsName,
                            cellAddress = request.cellAddress,
                            newWorkbook = null,
                            isError = true,
                            errorReport = wser
                        )
                    }
                )
            },
            failure = {
                DeleteCellResponse(
                    wbKey = wbk,
                    wsName = request.wsName,
                    cellAddress = request.cellAddress,
                    newWorkbook = null,
                    isError = true,
                    errorReport = it
                )
            }
        )
        return rt
    }

}
