package com.qxdzbc.p6.app.action.workbook.add_ws.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav

import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorksheetRMImp @Inject constructor(
    private val appStateMs: Ms<AppState>
) : CreateNewWorksheetRM {
    private var appState by appStateMs
    override fun makeRequest(req: AddWorksheetRequest): RseNav<AddWorksheetResponse> {
        val wbk = req.wbKey
        val rs = appState.getWbRs(wbk).flatMap { wb ->
            wb.addWsRs(req.worksheet).flatMap {
                Ok(AddWorksheetResponse(it.reRun()))
            }
        }.mapError { err ->
            err.withNav(wbk)
        }
        return rs
    }
}
