package com.qxdzbc.p6.app.action.app.create_new_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorkbookApplierImp @Inject constructor(
    private val stateContMs: Ms<StateContainer>,
    private val pickDefaultActiveWb: PickDefaultActiveWbAction,
    private val errorRouter:ErrorRouter,
) : CreateNewWorkbookApplier {

    private var stateCont by stateContMs
    private var globalWbCont by stateCont.wbContMs
    private var globalWbStateCont by stateCont.wbStateContMs

    override fun applyRes(res: CreateNewWorkbookResponse?,) {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.windowId, res.wbKey)
            } else {
                iapply(res.wb, res.windowId)
            }
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
            if (useNewWindow || windowWasEmptyBeforeAdding) {
                pickDefaultActiveWb.pickAndUpdateActiveWbPointer(wdMs.value)
            }
        }
    }

}
