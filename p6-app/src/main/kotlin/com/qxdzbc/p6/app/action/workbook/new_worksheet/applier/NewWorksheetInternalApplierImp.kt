package com.qxdzbc.p6.app.action.workbook.new_worksheet.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject

class NewWorksheetInternalApplierImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val errorRouter: ErrorRouter,
) : NewWorksheetInternalApplier {
    var appState by appStateMs

    override fun apply(newWb: Workbook, newWorksheetName: String): Rse<Unit> {
        appState = appState.replaceWb(newWb)
        val rt = appState.getWbStateMsRs(newWb.key)
            .onSuccess { wbStateMs ->
                wbStateMs.value = wbStateMs.value.refreshWsState().setNeedSave(true)
            }.onFailure {
                errorRouter.publishToApp(it)
            }.map { Unit }
        return rt
    }
}
