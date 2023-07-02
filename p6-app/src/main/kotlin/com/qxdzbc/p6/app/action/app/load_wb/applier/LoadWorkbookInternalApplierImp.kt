package com.qxdzbc.p6.app.action.app.load_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class LoadWorkbookInternalApplierImp @Inject constructor(
    val stateCont:StateContainer,
) : LoadWorkbookInternalApplier {
   
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
                    val wbkMs = wb.keyMs
                    globalWbStateCont.getWbStateMs(wbk)?.also {
                        it.value = it.value.setWindowId(windowId).setNeedSave(false)
                    }
                    windowStateMs.value = windowStateMs.value.addWbKey(wbkMs)
                }
                is Err ->{
                    // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id
                    val newWindowId = windowId ?: UUID.randomUUID().toString()
                    val  newOuterWindowStateMs = stateCont.createNewWindowStateMs(newWindowId)
                    val newWindowStateMs = newOuterWindowStateMs.value.innerWindowStateMs
                    globalWbStateCont.getWbStateMs(wb.key)?.also {
                        it.value = it.value.setWindowId(newWindowId).setNeedSave(false)
                        newWindowStateMs.value.activeWbPointerMs.value = newWindowStateMs.value.activeWbPointer.pointTo(it.value.wbKeyMs)
                    }
                    val s2 = newWindowStateMs.value.addWbKey(wb.keyMs)
                    newWindowStateMs.value = s2
                }
            }
        }
    }
}
