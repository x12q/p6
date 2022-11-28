package com.qxdzbc.p6.app.action.app.close_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.qxdzbc.p6.app.document.workbook.WorkbookKey

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookInternalApplierImp @Inject constructor(
    val appStateMs: Ms<AppState>,
    val stateContMs: Ms<StateContainer>,
    val pickDefaultActiveWb: PickDefaultActiveWbAction,
) : CloseWorkbookInternalApplier {

    private var stateCont by stateContMs

    private var appState by appStateMs
    private var scriptCont by stateCont.centralScriptContainerMs

    override fun apply(workbookKey: WorkbookKey?, windowId: String?) {
        val wbKeyMs = workbookKey?.let { stateCont.getWbKeyMs(it) }
        val wbKey = wbKeyMs?.value

        if (wbKey != null) {
            stateCont.wbCont = stateCont.wbCont.removeWb(wbKey)
            appState.codeEditorState = appState.codeEditorState.removeScriptOfWb(workbookKey)
            appState.translatorContainer = appState.translatorContainer.removeTranslator(workbookKey)
            scriptCont=scriptCont.removeScriptContFor(wbKey)
            val windowStateMs: Ms<WindowState>? =
                (windowId?.let { stateCont.getWindowStateMsById(it) }
                    ?: stateCont.getWindowStateMsByWbKey(wbKey))
            if (windowStateMs != null) {
                windowStateMs.value = windowStateMs.value.removeWbState(wbKeyMs)
                pickDefaultActiveWb.pickAndUpdateActiveWbPointer(windowStateMs.value)
            }
        }
    }
}
