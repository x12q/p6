package com.qxdzbc.p6.ui.window.tool_bar.color_selector.action

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.tool_bar.return_focus_to_cell.ReturnFocusToCellCursor
import com.qxdzbc.p6.di.action.TextColorSelectorActionQ
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
@TextColorSelectorActionQ
class TextColorSelectorAction @Inject constructor(
    private val stateContainerSt:StateContainer,
    val updateCellFormatAction: UpdateCellFormatAction,
    val returnFocusToCellCursor: ReturnFocusToCellCursor,
): ColorSelectorAction {

    private val sc  = stateContainerSt

    /**
     * Restore text of the current cell to default color
     */
    override fun clearColor(windowId: String) {
        pickColor(windowId,null)
    }

    /**
     * Color the text of the current cell with [color]
     */
    override fun pickColor(windowId: String, color: Color?) {
        updateCellFormatAction.setTextColorOnSelectedCells(color,undoable=true)
        sc.getTextColorSelectorStateMs(windowId)?.also {colorSelectorStateMs->
            colorSelectorStateMs.value = colorSelectorStateMs.value.setCurrentColor(color)
        }
        returnFocusToCellCursor.returnFocusToCurrentCellCursor()
    }
}
