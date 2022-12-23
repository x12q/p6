package com.qxdzbc.p6.ui.window.tool_bar.action

import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.tool_bar.return_focus_to_cell.ReturnFocusToCellCursor
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.action.TextSizeSelectorAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ToolBarActionImp @Inject constructor(
    override val textSizeSelectorAction: TextSizeSelectorAction,
) : ToolBarAction
