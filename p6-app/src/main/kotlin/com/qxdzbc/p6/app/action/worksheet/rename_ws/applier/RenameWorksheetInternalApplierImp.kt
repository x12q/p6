package com.qxdzbc.p6.app.action.worksheet.rename_ws.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.onSuccess
import com.github.michaelbull.result.unwrapError
import javax.inject.Inject

class RenameWorksheetInternalApplierImp
@Inject constructor(
    private val appStateMs: Ms<AppState>,
    private val errorRouter: ErrorRouter,
) : RenameWorksheetInternalApplier {
    var appState by appStateMs
    val translatorContainerMs: Ms<TranslatorContainer> = appState.translatorContMs
    var translatorCont by translatorContainerMs

    override fun apply(wbKey: WorkbookKey, oldName: String, newName: String) {
        val appState = appStateMs.value
        appState.queryStateByWorkbookKey(wbKey).ifOk {
            val wbState by it.workbookStateMs
            val wb = wbState.wb
            if (oldName != newName) {
                // x: rename the sheet in wb
                val renameRs = wb.renameWsRs(oldName, newName)
                if (renameRs is Ok) {
                    val newWb: Workbook = renameRs.value
                    // x: rename ws state
                    val sheetStateMs: Ms<WorksheetState>? = wbState.getWsStateMs(oldName)
                    if (sheetStateMs != null) {
                        newWb.getWsMsRs(newName).onSuccess { newWs->
                            sheetStateMs.value = sheetStateMs.value.setWsMs(newWs)
                        }
                    }
                    var newWbState: WorkbookState = wbState
                    // x: preserve active sheet pointer if it was pointing to the old name
                    appStateMs.value = appState.replaceWb(newWb)
                    if (newWbState.activeSheetPointer.isPointingTo(oldName)) {
                        newWbState = newWbState.setActiveSheet(newName).setNeedSave(true)
                    }
                    it.workbookStateMs.value = newWbState
                } else {
                    errorRouter.publishToWindow(renameRs.unwrapError(), it.windowState.id)
                }
            }
        }
    }
}
