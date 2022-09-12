package com.qxdzbc.p6.app.action.app.close_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.ui.window.state.WindowState
import javax.inject.Inject

class CloseWorkbookInternalApplierImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>,
    @StateContainerMs val stateContMs: Ms<StateContainer>,
    val pickDefaultActiveWb: PickDefaultActiveWbAction,
) : CloseWorkbookInternalApplier {

    private var stateCont by stateContMs

    private var appState by appStateMs
    private var scriptCont by stateCont.centralScriptContainerMs

    override fun apply(workbookKey: WorkbookKey?, windowId: String?) {
        val wbKey: WorkbookKey? = workbookKey
        val windowStateMs: Ms<WindowState>? =
            windowId?.let { stateCont.getWindowStateMsById(it) }
                ?: wbKey?.let { stateCont.getWindowStateMsByWbKey(it) }

        if (wbKey != null) {
            stateCont.wbCont = stateCont.wbCont.removeWb(wbKey)
            appState.codeEditorState = appState.codeEditorState.removeWorkbook(workbookKey)
            appState.translatorContainer = appState.translatorContainer.removeTranslator(workbookKey)
            scriptCont=scriptCont.removeScriptContFor(wbKey)
            if (windowStateMs != null) {
                windowStateMs.value = windowStateMs.value.removeWorkbookState(wbKey)
                pickDefaultActiveWb.pickAndUpdateActiveWbPointer(windowStateMs.value)
            }
        }
    }
}
