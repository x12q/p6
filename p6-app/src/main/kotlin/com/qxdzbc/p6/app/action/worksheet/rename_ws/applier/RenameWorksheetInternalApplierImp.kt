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
    private val docCont: DocumentContainer,
    private val errorRouter: ErrorRouter,
) : RenameWorksheetInternalApplier {

    private val dc = docCont

    override fun apply(wbKey: WorkbookKey, oldName: String, newName: String) {
        stateContainer.getStateByWorkbookKeyRs(wbKey).map {
            it.workbookStateMs?.let{workbookStateMs->
                it.windowState?.let{windowState->
                    val wb = workbookStateMs.wb
                    if (oldName != newName) {
                        // x: rename the sheet in wb
                        val renameRs = wb.renameWsRs(oldName, newName)
                        if (renameRs is Ok) {
                            val newWb: Workbook = renameRs.value
                            // x: rename ws state
                            val sheetStateMs: Ms<WorksheetState>? = workbookStateMs.getWsStateMs(oldName)
                            if (sheetStateMs != null) {
                                newWb.getWsMsRs(newName).onSuccess { newWs ->
                                    sheetStateMs.value = sheetStateMs.value.setWsMs(newWs)
                                }
                            }
                            val newWbState: WorkbookState = workbookStateMs
                            // x: preserve active sheet pointer if it was pointing to the old name
                            dc.replaceWb(newWb)
                            if (newWbState.activeSheetPointer.isPointingTo(oldName)) {
                                newWbState.setActiveSheet(newName)
                                newWbState.needSave = true
                            }
                        } else {
                            errorRouter.publishToWindow(renameRs.unwrapError(), windowState.id)
                        }
                    }
                }
            }
        }.onFailure {
            errorRouter.publishToWindow(it,wbKey)
        }
    }
}
