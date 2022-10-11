package com.qxdzbc.p6.app.action.cell_editor.color_formula

import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState

interface ColorFormulaInCellEditorAction {
    fun colorFormulaInCellEditor()
    fun colorFormulaInCellEditor(i:CellEditorState):CellEditorState
}
