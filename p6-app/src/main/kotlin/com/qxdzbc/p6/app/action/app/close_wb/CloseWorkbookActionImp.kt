package com.qxdzbc.p6.app.action.app.close_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppStateErrors
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookActionImp @Inject constructor(
    private val scMs: Ms<StateContainer>,
    private val pickDefaultActiveWb: PickDefaultActiveWbAction,
    private val errorRouter: ErrorRouter,
) : CloseWorkbookAction {

    private val sc by scMs
    private val appState by scMs.value.appStateMs

    override fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse {
        val response = requestCloseWb(request)
        closeWbApplier_applyRes(response)
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
        outputState.respectiveWindowState?.also { newWdState ->
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

    private fun closeWbApplier_applyRes(res: CloseWorkbookResponse?) {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.windowId, res.wbKey)
            } else {
                internalApply(res.wbKey, res.windowId)
            }
        }
    }

    fun internalApply(workbookKey: WorkbookKey?, windowId: String?) {
        val wbKeyMs = workbookKey?.let { sc.getWbKeyMs(it) }
        val wbKey = wbKeyMs?.value

        if (wbKey != null) {
            sc.wbContMs.value = sc.wbCont.removeWb(wbKey)
            appState.translatorContainer = appState.translatorContainer.removeTranslator(workbookKey)
            val windowStateMs: Ms<WindowState>? =
                (windowId?.let { sc.getWindowStateMsById(it) }
                    ?: sc.getWindowStateMsByWbKey(wbKey))
            if (windowStateMs != null) {
                windowStateMs.value = windowStateMs.value.removeWbState(wbKeyMs)
                pickDefaultActiveWb.pickAndUpdateActiveWbPointer(windowStateMs.value)
            }
        }
    }

    private var globalWbStateCont by sc.wbStateContMs

    fun requestCloseWb(request: CloseWorkbookRequest): CloseWorkbookResponse {
        val windowStateMs: Ms<WindowState>? = if (request.windowId != null) {
            sc.getWindowStateMsById(request.windowId)
        } else {
            sc.getWindowStateMsByWbKey(request.wbKey)
        }
        if (windowStateMs != null) {
            val getWbRs = globalWbStateCont.getWbStateMsRs(request.wbKey)
            when (getWbRs) {
                is Ok -> {
                    return CloseWorkbookResponse(
                        errorReport = null,
                        wbKey = request.wbKey,
                        windowId = request.windowId
                    )
                }

                is Err -> {
                    return CloseWorkbookResponse(
                        errorReport = getWbRs.error,
                        wbKey = request.wbKey,
                        windowId = request.windowId
                    )
                }
            }
        } else {
            return CloseWorkbookResponse(
                errorReport = AppStateErrors.InvalidWindowState.report1(request.wbKey),
                wbKey = request.wbKey,
                windowId = request.windowId
            )
        }
    }
}
