package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState

/**
 * make text that is displayed on the cell editor UI.
 * When the cell editor is extracting cell address from the range selector, display text = current text + range-selector address
 */
fun interface MakeCellEditorDisplayTextAction {
    fun makeRangeSelectorText(editorState: CellEditorState): TextFieldValue
}
