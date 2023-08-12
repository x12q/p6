package com.qxdzbc.p6.composite_actions.cell_editor.color_formula

import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState

/**
 * This action colors the current text in cell formula editor.
 */
interface ColorFormulaInCellEditorAction {
    /**
     * color the current text in the active cell editor
     */
    fun colorCurrentTextInCellEditor()
    /**
     * color the current test (not the display one) in a cell editor. Return a new [CellEditorState] containing the colored text
     */
    fun colorCurrentTextInCellEditor(cellEditorState:CellEditorState):CellEditorState

    /**
     * color the display text in a cell editor.Return a new [CellEditorState] containing the colored text
     */
    fun colorDisplayTextInCellEditor(cellEditorState: CellEditorState): CellEditorState
}
