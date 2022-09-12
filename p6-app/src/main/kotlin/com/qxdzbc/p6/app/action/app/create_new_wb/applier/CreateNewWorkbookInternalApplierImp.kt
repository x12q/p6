package com.qxdzbc.p6.app.action.app.create_new_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.ui.window.state.WindowState
import java.util.UUID
import javax.inject.Inject

class CreateNewWorkbookInternalApplierImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>,
    val pickDefaultActiveWb: PickDefaultActiveWbAction,
) : CreateNewWorkbookInternalApplier {
    private var stateCont by stateContMs
    private var scriptCont by stateCont.centralScriptContainerMs
    private var globalWbCont by stateCont.wbContMs
    private var globalWbStateCont by stateCont.wbStateContMs

    override fun apply(workbook: Workbook?, windowId: String?) {
        val wb = workbook
        if (wb != null) {
            val wbk = wb.key
            globalWbCont = globalWbCont.addOrOverWriteWb(wb)
            globalWbStateCont.getWbStateMs(wbk)?.also {
                it.value = it.value.setWindowId(windowId)
            }
            var useNewWindow = false
            val wdMs=stateCont.getWindowStateMsDefaultRs(windowId).component1() ?: run {
                // x: create new window state if no window is available
                val newWid = windowId ?: UUID.randomUUID().toString()
                val (newStateCont, newWindowState) = stateCont.createNewWindowStateMs(newWid)
                stateCont = newStateCont
                useNewWindow = true
                newWindowState
            }
            wdMs.value = wdMs.value.addWbKey(wbk)
            scriptCont = scriptCont.addScriptContFor(wb.key)
            if(useNewWindow){
                pickDefaultActiveWb.pickAndUpdateActiveWbPointer(wdMs.value)
            }
        }
    }
}
