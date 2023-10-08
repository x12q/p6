package com.qxdzbc.p6.composite_actions.app.close_wb

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.composite_actions.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppStateErrors
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookActionImp @Inject constructor(
    private val scMs:StateContainer,
    private val pickDefaultActiveWb: PickDefaultActiveWbAction,
    private val errorRouter: ErrorRouter,
) : CloseWorkbookAction {

    private val sc = scMs

    override fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse {
        val response = requestCloseWb(request)
        applyRes(response)
        return response
    }

    override fun closeWb(wbKeySt: St<WorkbookKey>) {

        val wbKey = wbKeySt.value

        val inputState = CloseWbState(
            wbCont = sc.wbCont,

            respectiveWindowState = sc.getWindowStateByWbKey(wbKey)
        )

        val outputState = closeWb(wbKeySt, inputState)

        outputState.wbCont

    }

    fun closeWb(wbKeySt: St<WorkbookKey>, inputState: CloseWbState): CloseWbState {
        val wbKey = wbKeySt.value
        inputState.wbCont.removeWb(wbKey)
        inputState.respectiveWindowState?.let {
            it.removeWbState(wbKeySt)
            pickDefaultActiveWb.pickAndUpdateActiveWbPointer(it)
        }
        return CloseWbState(
            wbCont = inputState.wbCont,
            respectiveWindowState = inputState.respectiveWindowState
        )
    }

    private fun applyRes(res: CloseWorkbookResponse?) {
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
            sc.wbCont.removeWb(wbKey)
            val windowState: WindowState? =
                (windowId?.let { sc.getWindowStateMsById(it) }
                    ?: sc.getWindowStateMsByWbKey(wbKey))
            if (windowState != null) {
                windowState.removeWbState(wbKeyMs)
                pickDefaultActiveWb.pickAndUpdateActiveWbPointer(windowState)
            }
        }
    }

    private val globalWbStateCont = sc.wbStateCont

    fun requestCloseWb(request: CloseWorkbookRequest): CloseWorkbookResponse {
        val windowState: WindowState? = if (request.windowId != null) {
            sc.getWindowStateMsById(request.windowId)
        } else {
            sc.getWindowStateMsByWbKey(request.wbKey)
        }
        if (windowState != null) {
            val getWbRs = globalWbStateCont.getWbStateRs(request.wbKey)
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
