package com.qxdzbc.p6.app.action.app.load_wb.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
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
    val stateCont: StateContainer,
) : LoadWorkbookInternalApplier {

    private var globalWbCont by stateCont.wbContMs
    private val globalWbStateCont = stateCont.wbStateCont

    override fun apply(windowId: String?, workbook: Workbook?) {
        val windowStateMsRs = stateCont.getWindowStateMs_OrDefault_OrCreateANewOne_Rs(windowId)

        workbook?.also { wb ->
            globalWbCont.addOrOverWriteWb(wb)
            when (windowStateMsRs) {
                is Ok -> {
                    val windowState = windowStateMsRs.value
                    val wbk = wb.key
                    val wbkMs = wb.keyMs
                    globalWbStateCont.getWbState(wbk)?.also {
                        it.let{
                            it.windowId = windowId
                            it.needSave = false
                        }
                    }
                    windowState.addWbKey(wbkMs)
                }

                is Err -> {
                    // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id
                    val newWindowId = windowId ?: UUID.randomUUID().toString()
                    val newOuterWindowStateMs = stateCont.createNewWindowStateMs(newWindowId)
                    val newWindowStateMs = newOuterWindowStateMs.value.innerWindowState
                    globalWbStateCont.getWbState(wb.key)?.also {
                        it.let {
                            it.windowId = newWindowId
                            it.needSave = false
                        }
                        newWindowStateMs.activeWbPointerMs.value =
                            newWindowStateMs.activeWbPointer.pointTo(it.wbKeyMs)
                    }
                    newWindowStateMs.addWbKey(wb.keyMs)
                }
            }
        }
    }
}
