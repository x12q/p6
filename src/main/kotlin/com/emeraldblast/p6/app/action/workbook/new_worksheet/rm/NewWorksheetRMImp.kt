package com.emeraldblast.p6.app.action.workbook.new_worksheet.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo.Companion.toErr
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.emeraldblast.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookErrors
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.mapBoth
import javax.inject.Inject

class NewWorksheetRMImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>
) : NewWorksheetRM {

    var appState by appStateMs

    override fun newWorksheet2(request: CreateNewWorksheetRequest): RseNav<CreateNewWorksheetResponse2> {
        val wbk = request.wbKey
        val wbrs = appState.getWbRs(wbk)
        val rt = wbrs.mapBoth(
            success = { wb ->
                val canAdd = request.newWorksheetName?.let { !wb.containSheet(it) } ?: true
                val z = if (canAdd) {
                    val q = wb.createNewWs2(request.newWorksheetName)
                    Ok(
                        CreateNewWorksheetResponse2(
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
        return rt
    }

}
