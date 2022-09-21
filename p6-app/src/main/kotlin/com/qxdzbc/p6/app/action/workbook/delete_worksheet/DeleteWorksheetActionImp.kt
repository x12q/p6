package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rs
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRM
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.state.app_state.AppStateMs

import com.qxdzbc.p6.rpc.workbook.WorkbookRpcMsgErrors
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import javax.inject.Inject

class DeleteWorksheetActionImp @Inject constructor(
    val rm:DeleteWorksheetRM,
    val applier:DeleteWorksheetApplier,
    @AppStateMs
    private val appStateMs:Ms<AppState>
) : DeleteWorksheetAction {

    private var appState by appStateMs

    override fun deleteWorksheetRs(request: WorksheetIdWithIndexPrt): Rs<Unit, ErrorReport> {
        if (request.wsName != null || request.wsIndex != null) {
            val res = rm.makeRequest(request)
            when(res){
                is Ok ->{
                    if (request.wsName != null) {
                        return applier.applyResRs(request.wsName, res)
                    } else {
                        return appState.getWbRs(request.wbKey)
                            .andThen { wb ->
                                wb.getWsRs(request.wsIndex!!)
                            }.andThen {ws->
                                val wsName = ws.name
                                applier.applyResRs(wsName, res)
                            }
                    }
                }
                is Err ->{
                    // do nothing
                    return res
                }
            }

        } else {
            return WorkbookRpcMsgErrors.IllegalMsg.report("IdentifyWorksheetMsg must contain at least a worksheet name or a worksheet index")
                .toErr()
        }
    }
}
