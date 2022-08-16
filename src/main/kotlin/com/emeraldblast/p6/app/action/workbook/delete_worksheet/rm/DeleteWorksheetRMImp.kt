package com.emeraldblast.p6.app.action.workbook.delete_worksheet.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.rpc.document.workbook.msg.IdentifyWorksheetMsg
import com.emeraldblast.p6.rpc.document.workbook.WorkbookRpcMsgErrors
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import javax.inject.Inject

class DeleteWorksheetRMImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>
) : DeleteWorksheetRM {
    var appState by appStateMs

    override fun makeRequest(request: IdentifyWorksheetMsg): Rse<Workbook> {
        val wbKey = request.wbKey
        val name = request.wsName
        val index = request.wsIndex
        if (name != null) {
            return this.deleteWorksheetRs(wbKey, name)
        } else if (index != null) {
            return this.deleteWorksheetRs(wbKey, index)
        } else {
            return WorkbookRpcMsgErrors.IllegalMsg
                .report("IdentifyWorksheetMsg must contain at least a worksheet name or a worksheet index")
                .toErr()
        }
    }

     private fun deleteWorksheetRs(wbKey: WorkbookKey, wsName: String): Rse<Workbook> {
        val wbRs = appState.getWorkbookRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsName).map { it.reRun() }
        }
        return rt
    }

    private fun deleteWorksheetRs(wbKey: WorkbookKey, wsIndex: Int): Rse<Workbook> {
        val wbRs = appState.getWorkbookRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsIndex).map { it.reRun() }
        }
        return rt
    }
}
