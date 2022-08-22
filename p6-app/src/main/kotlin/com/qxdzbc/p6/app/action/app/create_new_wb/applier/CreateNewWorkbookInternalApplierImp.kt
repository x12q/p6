package com.qxdzbc.p6.app.action.app.create_new_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.window.state.WindowState
import javax.inject.Inject

class CreateNewWorkbookInternalApplierImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>
) : CreateNewWorkbookInternalApplier {
    private var stateCont by stateContMs
    private var subStateCont by stateContMs
    private var scriptCont by stateCont.centralScriptContainerMs
    private var globalWbCont by stateCont.globalWbContMs
    private var globalWbStateCont by stateCont.globalWbStateContMs

    override fun apply(workbook: Workbook?, windowId: String?) {
        val wb = workbook

        if (wb != null) {
            val wbk = wb.key
            globalWbCont = globalWbCont.addOrOverWriteWb(wb)
            globalWbStateCont.getWbStateMs(wbk)?.also {
                it.value = it.value.setWindowId(windowId)
            }
            if (windowId != null) {
                val wdMs:Ms<WindowState>? = subStateCont.getWindowStateMsById(windowId)
                if (wdMs != null) {
                    // x: create state new wb state
                    wdMs.value = wdMs.value.addWbKey(wbk)
                } else {
                    // x: create new window state if no window is available
                    val (newStateCont, newWindowState) = subStateCont.createNewWindowStateMs(windowId)
                    subStateCont = newStateCont
                    newWindowState.value = newWindowState.value.addWbKey(wbk)
                }

                scriptCont = scriptCont.addScriptContFor(wb.key)

            } else {
                val (newAppState, newWindowState) = subStateCont.createNewWindowStateMs()
                subStateCont = newAppState
                newWindowState.value = newWindowState.value.addWbKey(wbk)
            }
        }
    }
}
