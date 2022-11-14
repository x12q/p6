package com.qxdzbc.p6.app.action.cell_editor.color_formula

import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState

interface ColorFormulaInCellEditorAction {
    /**
     * color the current text in the cell editor
     */
    fun colorCurrentTextInCellEditor()
    /**
     * color the current test (not the display one) in a cell editor
     */
    fun colorCurrentTextInCellEditor(cellEditorState:CellEditorState):CellEditorState

    /**
     * color the display text in a cell editor
     */
    fun colorDisplayTextInCellEditor(cellEditorState: CellEditorState): CellEditorState
}
