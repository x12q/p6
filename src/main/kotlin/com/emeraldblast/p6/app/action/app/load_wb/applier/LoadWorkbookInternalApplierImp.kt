package com.emeraldblast.p6.app.action.app.load_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.di.state.app_state.StateContainerMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.StateContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import java.util.*
import javax.inject.Inject

class LoadWorkbookInternalApplierImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>,
) : LoadWorkbookInternalApplier {
    private var stateCont by stateContMs
    private var globalWbCont by stateCont.globalWbContMs
    private var globalWbStateCont by stateCont.globalWbStateContMs

    override fun apply(windowId: String?, workbook: Workbook?) {
        val windowStateMs = windowId?.let { stateCont.getWindowStateMsById(it) }
        workbook?.also { wb ->
            globalWbCont = globalWbCont.addOrOverWriteWb(wb)
            if (windowStateMs != null) {
                // x: add the loaded workbook to the window state
                // x: publish err if response is erroneous
                val wbk = wb.key
                globalWbStateCont.getWbStateMs(wbk)?.also {
                    it.value = it.value.setWindowId(windowId).setNeedSave(false)
                }
                windowStateMs.value = windowStateMs.value.addWbKey(wbk)
            } else {
                // x: designated window does not exist => create a new window for the loaded workbook with the provided window id
                val newWindowId = windowId ?: UUID.randomUUID().toString()
                val (newStateCont, newWindowStateMs) = stateCont.createNewWindowStateMs(newWindowId)
                stateCont = newStateCont
                globalWbStateCont.getWbStateMs(wb.key)?.also {
                    it.value = it.value.setWindowId(newWindowId).setNeedSave(false)
                }
                val s2 = newWindowStateMs.value.addWbKey(wb.key)
                newWindowStateMs.value = s2
            }
        }
    }
}
