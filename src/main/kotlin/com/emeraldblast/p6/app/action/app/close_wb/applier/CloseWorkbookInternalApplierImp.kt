package com.emeraldblast.p6.app.action.app.close_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.document.workbook.action.WorkbookActionTable
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.window.state.WindowState
import javax.inject.Inject

class CloseWorkbookInternalApplierImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>,
    private val wbActionTable: WorkbookActionTable,
) : CloseWorkbookInternalApplier {

    private var appState by appStateMs
    private var scriptCont by appState.centralScriptContainerMs

    override fun apply(workbookKey: WorkbookKey?, windowId: String?) {
        val wbKey: WorkbookKey? = workbookKey
        val windowStateMs: Ms<WindowState>? =
            windowId?.let { appState.getWindowStateMsById(it) }
                ?: wbKey?.let { appState.getWindowStateMsByWbKey(it) }

        if (wbKey != null) {
            if (windowStateMs != null) {
                windowStateMs.value = windowStateMs.value.removeWorkbookState(wbKey)
            }
            appState.globalWbCont = appState.globalWbCont.removeWb(wbKey)
            appState.globalWbStateCont = appState.globalWbStateCont.removeWbState(wbKey)
            appState.codeEditorState = appState.codeEditorState.removeWorkbook(workbookKey)
            appState.translatorContainer = appState.translatorContainer.removeTranslator(workbookKey)

            scriptCont=scriptCont.removeScriptContFor(wbKey)
        }
    }
}
