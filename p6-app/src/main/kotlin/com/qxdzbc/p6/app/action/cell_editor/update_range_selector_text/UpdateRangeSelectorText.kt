package com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text

import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState

interface UpdateRangeSelectorText {
    fun updateRangeSelectorTextInCurrentCellEditor()
    fun updateRangeSelectorText(cellEditorState:CellEditorState):CellEditorState
}



