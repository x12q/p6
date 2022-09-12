package com.qxdzbc.p6.app.action.window.pick_active_wb

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import org.checkerframework.checker.nullness.qual.NonNull
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

class PickDefaultActiveWbActionImp @Inject constructor(
    @StateContainerSt
    val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : PickDefaultActiveWbAction {
    override fun pickAndUpdateActiveWbPointer(windowState: WindowState) {
        val ws = windowState
        if (!ws.activeWorkbookPointer.isValid()) {
            val newPointer = ws.wbKeySet.firstOrNull()?.let {
                ws.activeWorkbookPointer.pointTo(it)
            } ?: ws.activeWorkbookPointer
            windowState.activeWorkbookPointer = newPointer
        }
    }

    override fun pickAndUpdateActiveWbPointer(windowId: String?) {
        windowId?.let { stateContSt.value.getWindowStateById(it) }
            ?.also { windowState ->
                pickAndUpdateActiveWbPointer(windowState)
            }
    }
}
