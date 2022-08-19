package com.emeraldblast.p6.app.action.workbook.delete_worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRM
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.di.state.app_state.AppStateMs

import com.emeraldblast.p6.rpc.document.workbook.WorkbookRpcMsgErrors
import com.emeraldblast.p6.rpc.document.workbook.msg.IdentifyWorksheetMsg
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import javax.inject.Inject

class DeleteWorksheetActionImp @Inject constructor(
    val rm:DeleteWorksheetRM,
    val applier:DeleteWorksheetApplier,
    @AppStateMs
    private val appStateMs:Ms<AppState>
) : DeleteWorksheetAction {

    private var appState by appStateMs

    override fun deleteWorksheetRs(request: IdentifyWorksheetMsg): Rs<Unit, ErrorReport> {
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
