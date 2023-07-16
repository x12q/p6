package com.qxdzbc.p6.app.action.app.create_new_wb

import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookFactory
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorkbookActionImp @Inject constructor(
    private val pickDefaultActiveWb: PickDefaultActiveWbAction,
    private val errorRouter: ErrorRouter,
    private val stateCont: StateContainer,
    private val wbf: WorkbookFactory,
) : CreateNewWorkbookAction {


    private val wbCont = stateCont.wbCont
    private val wbStateCont = stateCont.wbStateCont

    override fun createNewWb(request: CreateNewWorkbookRequest): CreateNewWorkbookResponse {
        val rt = wbf.createWbRs(request.wbName)
        val q = rt.mapBoth(
            success = { newWb ->
                apply2(newWb, request.windowId)
                CreateNewWorkbookResponse(null, newWb, request.windowId)
            },
            failure = { err ->
                errorRouter.publishToWindow(err, request.windowId)
                CreateNewWorkbookResponse(err, null, request.windowId)
            }
        )
        return q
    }

    fun apply2(wb: Workbook, windowId: String?): Rse<Unit> {
        val rt = wbCont.addWbRs(wb)
            .onSuccess {
                wbStateCont.getWbState(wb.key)?.windowId = windowId
                var useNewWindow = false
                /*
                 * If the request contains a non-existing window id, a new window will be created with that id to hold the newly create wb.
                 * If the request contains null window id, a default window will be picked (active window, then first window) if possible, if no window is available, a new window will be created.
                 */
                val windowState = stateCont.getWindowState_OrDefault_Rs(windowId).component1() ?: run {
                    // x: only create new window state if no window is available
                    val newWindowId = windowId ?: UUID.randomUUID().toString()
                    val newWindowState = stateCont.createNewWindowStateMs(newWindowId)
                    useNewWindow = true
                    newWindowState.value.innerWindowState
                }
                val windowWasEmptyBeforeAdding = windowState.isEmpty()
                windowState.addWbKey(wb.keyMs)
                if (useNewWindow || windowWasEmptyBeforeAdding) {
                    pickDefaultActiveWb.pickAndUpdateActiveWbPointer(windowState)
                }
            }
        return rt
    }
}
