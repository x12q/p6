package com.qxdzbc.p6.app.action.app.load_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import java.util.*
import javax.inject.Inject

class LoadWorkbookInternalApplierImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>,
) : LoadWorkbookInternalApplier {
    private var stateCont by stateContMs
    private var globalWbCont by stateCont.wbContMs
    private var globalWbStateCont by stateCont.wbStateContMs

    override fun apply(windowId: String?, workbook: Workbook?) {
        val windowStateMsRs =  stateCont.getWindowStateMsDefaultRs(windowId)

        workbook?.also { wb ->
            globalWbCont = globalWbCont.addOrOverWriteWb(wb)
            when(windowStateMsRs){
                is Ok ->{
                    val windowStateMs = windowStateMsRs.value
                    val wbk = wb.key
                    globalWbStateCont.getWbStateMs(wbk)?.also {
                        it.value = it.value.setWindowId(windowId).setNeedSave(false)
                    }
                    windowStateMs.value = windowStateMs.value.addWbKey(wbk)
                }
                is Err ->{
                    // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id
                    val newWindowId = windowId ?: UUID.randomUUID().toString()
                    val (newStateCont, newWindowStateMs) = stateCont.createNewWindowStateMs(newWindowId)
                    stateCont = newStateCont
                    globalWbStateCont.getWbStateMs(wb.key)?.also {
                        it.value = it.value.setWindowId(newWindowId).setNeedSave(false)
                        newWindowStateMs.value.activeWorkbookPointer = newWindowStateMs.value.activeWorkbookPointer.pointTo(it.value.wbKey)
                    }
                    val s2 = newWindowStateMs.value.addWbKey(wb.key)
                    newWindowStateMs.value = s2
                }
            }
        }
    }
}
