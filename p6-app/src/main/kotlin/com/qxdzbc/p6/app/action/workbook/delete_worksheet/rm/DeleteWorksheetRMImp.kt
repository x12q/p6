package com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.document.workbook.WorkbookRpcMsgErrors
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt
import javax.inject.Inject

class DeleteWorksheetRMImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>
) : DeleteWorksheetRM {
    var appState by appStateMs

    override fun makeRequest(request: WorksheetIdPrt): Rse<Workbook> {
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
        val wbRs = appState.getWbRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsName).map { it.reRun() }
        }
        return rt
    }

    private fun deleteWorksheetRs(wbKey: WorkbookKey, wsIndex: Int): Rse<Workbook> {
        val wbRs = appState.getWbRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsIndex).map { it.reRun() }
        }
        return rt
    }
}
