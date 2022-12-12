package com.qxdzbc.p6.app.action.app.close_wb

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplier
import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRM
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookActionImp @Inject constructor(
    private val closeWbRm: CloseWorkbookRM,
    private val closeWbApplier: CloseWorkbookApplier,
    private val scMs: Ms<StateContainer>,
    private val pickDefaultActiveWb: PickDefaultActiveWbAction,
) : CloseWorkbookAction {

    private val sc by scMs

    override fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse {
        val response = closeWbRm.closeWb(request)
        closeWbApplier.applyRes(response)
        return response
    }

    override fun closeWb(wbKeySt: St<WorkbookKey>) {

        val wbKey = wbKeySt.value
        val appState by sc.appStateMs

        val inputState = CloseWbState(
            wbCont = sc.wbCont,
            translatorContainer = appState.translatorContainer,
            respectiveWindowState = sc.getWindowStateByWbKey(wbKey)
        )

        val outputState = closeWb(wbKeySt, inputState)

        sc.wbCont = outputState.wbCont
        appState.translatorContainer = outputState.translatorContainer
        outputState.respectiveWindowState?.also {newWdState->
            sc.getWindowStateMsById(newWdState.id)?.also {
                it.value = newWdState
            }
        }
    }

    fun closeWb(wbKeySt: St<WorkbookKey>, inputState: CloseWbState): CloseWbState {
        val wbKey = wbKeySt.value
        val newWbCont = inputState.wbCont.removeWb(wbKey)
        val newTranslatorContainer = inputState.translatorContainer.removeTranslator(wbKey)
        val newWindowState = inputState.respectiveWindowState?.let {
            val newWindowState = it.removeWbState(wbKeySt)
            pickDefaultActiveWb.pickAndUpdateActiveWbPointer(it)
            newWindowState
        }
        return CloseWbState(
            wbCont = newWbCont,
            translatorContainer = newTranslatorContainer,
            respectiveWindowState = newWindowState
        )
    }
}
