package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState

/**
 * The purpose of this wrapping layer is to prevent the forced-push-to-top effect when updating app, window, wb states
 */
interface OuterWindowState {
    val innerWindowStateMs:Ms<WindowState>
    var innerWindowState:WindowState
    val id: String
    var focusState:WindowFocusState
    val errorContainer:ErrorContainer
}
