package com.qxdzbc.p6.ui.window.tool_bar.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorState
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorStateImp

class ToolBarStateImp constructor(
    override val textSizeSelectorStateMs: Ms<TextSizeSelectorState> = ms(TextSizeSelectorStateImp()),
) : ToolBarState {
    override val textSizeSelectorState: TextSizeSelectorState
        get() = textSizeSelectorStateMs.value
}
