package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState

/**
 * Extract the [TextFieldValue] for the current [CellEditorState]
 */
fun interface MakeCellEditorDisplayText {
    fun makeRangeSelectorText(editorState: CellEditorState): TextFieldValue
}
