package com.qxdzbc.p6.ui.window.tool_bar.action

import com.qxdzbc.p6.ui.window.tool_bar.color_selector.action.ColorSelectorAction
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action.TextSizeSelectorAction

interface ToolBarAction{
    val textSizeSelectorAction:TextSizeSelectorAction
    val textColorSelectorAction:ColorSelectorAction
    val cellBackgroundColorSelectorAction:ColorSelectorAction
}
