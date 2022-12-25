package com.qxdzbc.p6.ui.window.tool_bar.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.state.ColorSelectorState
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.state.ColorSelectorStateImp
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorState
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorStateImp

class ToolBarStateImp constructor(
    override val textSizeSelectorStateMs: Ms<TextSizeSelectorState> = ms(TextSizeSelectorStateImp()),
    override val textColorSelectorStateMs: Ms<ColorSelectorState> = ms(ColorSelectorStateImp()),
    override val cellBackgroundColorSelectorStateMs: Ms<ColorSelectorState> = ms(ColorSelectorStateImp()),
) : ToolBarState {
    override val textSizeSelectorState: TextSizeSelectorState
        get() = textSizeSelectorStateMs.value
    override val textColorSelectorState: ColorSelectorState
        get() = textColorSelectorStateMs.value
    override val cellBackgroundColorSelectorState: ColorSelectorState
        get() = cellBackgroundColorSelectorStateMs.value
}
