package com.qxdzbc.p6.app.action.window.pick_active_wb

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class PickDefaultActiveWbActionImp @Inject constructor(
    val stateCont:StateContainer
) : PickDefaultActiveWbAction {
    override fun pickAndUpdateActiveWbPointer(windowState: WindowState) {
        val ws = windowState
        if (!ws.activeWbPointer.isValid()) {
            val newPointer = ws.wbKeyMsSet.firstOrNull()?.let {
                ws.activeWbPointer.pointTo(it)
            } ?: ws.activeWbPointer
            windowState.activeWbPointerMs.value = newPointer
        }
    }

    override fun pickAndUpdateActiveWbPointer(windowId: String?) {
        windowId?.let { stateCont.getWindowStateById(it) }
            ?.also { windowState ->
                pickAndUpdateActiveWbPointer(windowState)
            }
    }
}
