package com.qxdzbc.p6.app.action.worksheet.rename_ws.applier

import com.github.michaelbull.result.*

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RenameWorksheetInternalApplierImp
@Inject constructor(
    val appState:AppState,
    val stateContainer: StateContainer,
    private val errorRouter: ErrorRouter,
) : RenameWorksheetInternalApplier {

    override fun apply(wbKey: WorkbookKey, oldName: String, newName: String) {
        stateContainer.getWsMsRs(wbKey,oldName).onSuccess {wsMs->
            wsMs.value = wsMs.value.setWsName(newName)
        }.onFailure {
            errorRouter.publishToWindow(it,wbKey)
        }
    }
}
