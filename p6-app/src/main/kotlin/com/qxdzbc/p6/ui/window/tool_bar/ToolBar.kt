package com.qxdzbc.p6.ui.window.tool_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.common_objects.P6Icons
import com.qxdzbc.p6.ui.common.view.MRow
import com.qxdzbc.p6.ui.window.tool_bar.action.ToolBarAction
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.ColorSelector
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.TextSizeSelectorView
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState

@Composable
fun ToolBar(
    windowId:String,
    state:ToolBarState,
    action: ToolBarAction
) {
    MRow(
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ){
        TextSizeSelectorView(
            windowId = windowId,
            state = state.textSizeSelectorState,
            action = action.textSizeSelectorAction,
        )

        ColorSelector(
            windowId=windowId,
            state = state.textColorSelectorState,
            action = action.textColorSelectorAction,
            icon  = P6Icons.FormatColorText
        )

        ColorSelector(
            windowId=windowId,
            state = state.cellBackgroundColorSelectorState,
            action = action.cellBackgroundColorSelectorAction,
            icon  = P6Icons.FormatColorFill
        )
    }
}
