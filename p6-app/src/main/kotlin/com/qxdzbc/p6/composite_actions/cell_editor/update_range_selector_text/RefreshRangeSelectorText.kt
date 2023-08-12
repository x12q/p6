package com.qxdzbc.p6.composite_actions.cell_editor.update_range_selector_text

import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState

/**
 * Refresh cell editor text that depends on range selector. This effectively generate a new text based on the current state of range selector.
 */
interface RefreshRangeSelectorText {
    /**
     * Produce a new [CellEditorState] with new range selector text, and replace the currently activated (on screen) [CellEditorState] with it.
     */
    fun refreshRangeSelectorTextInCurrentCellEditor()

    /**
     * Produce a new [CellEditorState] from [cellEditorState] with a new range selector text
     */
    fun refreshRangeSelectorText(cellEditorState:CellEditorState):CellEditorState
}



