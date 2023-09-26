package com.qxdzbc.p6.composite_actions.workbook.new_worksheet

import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.common.err.ErrorReportWithNavInfo.Companion.toErr
import com.qxdzbc.p6.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.common.err.ErrorReportWithNavInfos.noNav
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookErrors
import com.qxdzbc.p6.di.P6AnvilScope


import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class NewWorksheetActionImp @Inject constructor(
    private val errorRouter: ErrorRouter,
    val appState:AppState,
    private val sc: StateContainer,
    private val docCont: DocumentContainer,
) : NewWorksheetAction {

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
                    val q = wb.createNewWsWithMoreDetail(request.newWorksheetName)
                    Ok(
                        CreateNewWorksheetResponse(
                            newWb = q!!.newWb,
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
            docCont.replaceWb(newWb)
            sc.getWbStateRs(newWb.key)
                .onSuccess { wbStateMs ->
                    wbStateMs.refreshWsState()
                    wbStateMs.needSave = true
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
