package com.emeraldblast.p6.app.action.app.create_new_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.window.state.WindowState
import javax.inject.Inject

class CreateNewWorkbookInternalApplierImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>
) : CreateNewWorkbookInternalApplier {

    private var appState by appStateMs
    private var stateCont by appState.subAppStateContMs
    private var scriptCont by appState.centralScriptContainerMs
    private var globalWbCont by appState.globalWbContMs
    private var globalWbStateCont by appState.globalWbStateContMs

    override fun apply(workbook: Workbook?, windowId: String?) {
        val wb = workbook

        if (wb != null) {
            val wbk = wb.key
            globalWbCont = globalWbCont.addOrOverWriteWb(wb)
            globalWbStateCont.getWbStateMs(wbk)?.also {
                it.value = it.value.setWindowId(windowId)
            }
            if (windowId != null) {
                val wdMs:Ms<WindowState>? = stateCont.getWindowStateMsById(windowId)
                if (wdMs != null) {
                    // x: create state new wb state
                    wdMs.value = wdMs.value.addWbKey(wbk)
                } else {
                    // x: create new window state if no window is available
                    val (newStateCont, newWindowState) = stateCont.createNewWindowStateMs(windowId)
                    stateCont = newStateCont
                    newWindowState.value = newWindowState.value.addWbKey(wbk)
                }

                scriptCont = scriptCont.addScriptContFor(wb.key)

            } else {
                val (newAppState, newWindowState) = stateCont.createNewWindowStateMs()
                stateCont = newAppState
                newWindowState.value = newWindowState.value.addWbKey(wbk)
            }
        }
    }
}
