package com.qxdzbc.p6.app.action.window.pick_active_wb

import com.qxdzbc.common.compose.St

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import javax.inject.Inject

class PickDefaultActiveWbActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : PickDefaultActiveWbAction {
    override fun pickAndUpdateActiveWbPointer(windowState: WindowState) {
        val ws = windowState
        if (!ws.activeWbPointer.isValid()) {
            val newPointer = ws.wbKeyMsSet.firstOrNull()?.let {
                ws.activeWbPointer.pointTo(it)
            } ?: ws.activeWbPointer
            windowState.activeWbPointer = newPointer
        }
    }

    override fun pickAndUpdateActiveWbPointer(windowId: String?) {
        windowId?.let { stateContSt.value.getWindowStateById(it) }
            ?.also { windowState ->
                pickAndUpdateActiveWbPointer(windowState)
            }
    }
}
