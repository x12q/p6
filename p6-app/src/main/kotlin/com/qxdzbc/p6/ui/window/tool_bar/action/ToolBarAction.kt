package com.qxdzbc.p6.ui.window.tool_bar.action

import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.tool_bar.return_focus_to_cell.ReturnFocusToCellCursor
import com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.action.TextSizeSelectorAction

interface ToolBarAction{
    val textSizeSelectorAction:TextSizeSelectorAction
}
