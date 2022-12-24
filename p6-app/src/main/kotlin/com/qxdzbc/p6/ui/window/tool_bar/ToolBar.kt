package com.qxdzbc.p6.ui.window.tool_bar

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.qxdzbc.p6.ui.window.tool_bar.action.ToolBarAction
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.TextSizeSelector
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState

@Composable
fun ToolBar(
    windowId:String,
    state:ToolBarState,
    action: ToolBarAction
) {
    Row{
        TextSizeSelector(
            windowId = windowId,
            state = state.textSizeSelectorState,
            action = action.textSizeSelectorAction,
        )
    }
}
