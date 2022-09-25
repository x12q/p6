package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.compose.Ms

interface OuterWindowState {
    val innerWindowStateMs:Ms<WindowState>
    var innerWindowState:WindowState
    val id: String
}
