package com.qxdzbc.p6.app.action.workbook.new_worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.toErr
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfos.noNav
import com.qxdzbc.p6.app.document.workbook.WorkbookErrors

import com.qxdzbc.p6.di.state.app_state.DocumentContainerMs

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class NewWorksheetActionImp @Inject constructor(
    private val errorRouter: ErrorRouter,
    val appStateMs: Ms<AppState>,
    private val scMs: Ms<StateContainer>,
    @DocumentContainerMs private val dcMs: Ms<DocumentContainer>,
) : NewWorksheetAction {
    var sc by scMs
    var dc by dcMs
    var appState by appStateMs

    override fun createNewWorksheetRs(
        request: CreateNewWorksheetRequest,
        publishError: Boolean
    ): Rse<CreateNewWorksheetResponse> {
        val wbk = request.wbKey
        val getWbStateRs = sc.getWbStateRs(wbk)
        val rt = getWbStateRs.mapBoth(
            success = { wbState ->
                val wb = wbState.wb
                val canAdd = request.newWorksheetName?.let { !wb.containSheet(it) } ?: true
                val z = if (canAdd) {
                    val q = wb.createNewWs_MoreDetail(request.newWorksheetName)
                    Ok(
                        CreateNewWorksheetResponse(
                            newWb = q.newWb,
                            newWsName = q.newWsName,
                        )
                    )
                } else {
                    WorkbookErrors.WorksheetAlreadyExist.report(request.newWorksheetName ?: "null").withNav(wbk).toErr()
                }
                z
            },
            failure = {
                it.withNav(wbk).toErr()
            }
        )
        rt.map {
            val newWb = it.newWb
            dcMs.value = dc.replaceWb(newWb)
            sc.getWbStateMsRs(newWb.key)
                .onSuccess { wbStateMs ->
                    wbStateMs.value = wbStateMs.value.refreshWsState().setNeedSave(true)
                }
        }
        if (publishError) {
            rt.onFailure {
                errorRouter.publishToWindow(it.errorReport, it.windowId, wbk)
            }
        }
        return rt.noNav()
    }
}
