package com.qxdzbc.p6.ui.window.tool_bar.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.state.TextSizeSelectorState

interface ToolBarState {
    val textSizeSelectorStateMs:Ms<TextSizeSelectorState>
    val textSizeSelectorState: TextSizeSelectorState
}
