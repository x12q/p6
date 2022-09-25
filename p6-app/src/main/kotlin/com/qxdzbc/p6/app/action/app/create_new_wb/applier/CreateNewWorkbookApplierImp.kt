package com.qxdzbc.p6.app.action.app.create_new_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import java.util.*
import javax.inject.Inject

class CreateNewWorkbookApplierImp @Inject constructor(
    private val baseApplier: BaseApplier,
    @StateContainerMs private val stateContMs: Ms<StateContainer>,
    private val pickDefaultActiveWb: PickDefaultActiveWbAction,
) : CreateNewWorkbookApplier {

    private var stateCont by stateContMs
    private var scriptCont by stateCont.centralScriptContainerMs
    private var globalWbCont by stateCont.wbContMs
    private var globalWbStateCont by stateCont.wbStateContMs

    override fun applyRes(res: CreateNewWorkbookResponse?,) {
        baseApplier.applyRes(res) {
            iapply(it.wb, it.windowId)
        }
    }

    fun iapply(workbook: Workbook?, windowId: String?) {
        val wb = workbook
        if (wb != null) {
            val wbk = wb.key
            val wbkMs = wb.keyMs
            globalWbCont = globalWbCont.addOrOverWriteWb(wb)
            globalWbStateCont.getWbStateMs(wbk)?.also {
                it.value = it.value.setWindowId(windowId)
            }
            var useNewWindow = false
            /*
             * If the request contains a non-existing window id, a new window will be created with that id to hold the newly create wb.
             * If the request contains null window id, a default window will be picked (active window, then first window) if possible, if no window is available, a new window will be created.
             */
            val oWdMs = stateCont.getWindowStateMsDefaultRs(windowId).component1() ?: run {
                // x: only create new window state if no window is available
                val newWid = windowId ?: UUID.randomUUID().toString()
                val (newStateCont, newWindowState) = stateCont.createNewWindowStateMs(newWid)
                stateCont = newStateCont
                useNewWindow = true
                newWindowState.value.innerWindowStateMs
            }
            val wdMs = oWdMs
            val windowWasEmptyBeforeAdding = wdMs.value.isEmpty()
            wdMs.value = wdMs.value.addWbKey(wbkMs)
            scriptCont = scriptCont.addScriptContFor(wb.key)
            if (useNewWindow || windowWasEmptyBeforeAdding) {
                pickDefaultActiveWb.pickAndUpdateActiveWbPointer(wdMs.value)
            }
        }
    }

}
