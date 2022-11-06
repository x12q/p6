package com.qxdzbc.p6.app.action.cell_editor.color_formula

import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState

interface ColorFormulaInCellEditorAction {
    /**
     * format (color, etc) the current formula (not the display one) in a formula
     */
    fun formatCurrentFormulaInCellEditor()
    /**
     * format (color, etc) the current formula (not the display one) in a formula
     */
    fun formatCurrentFormulaInCellEditor(i:CellEditorState):CellEditorState
}
