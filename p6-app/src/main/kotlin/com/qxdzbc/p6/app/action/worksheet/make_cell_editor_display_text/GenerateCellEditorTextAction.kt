package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId

/**
 * make texts for on the cell editor UI.
 */
interface GenerateCellEditorTextAction {
    /**
     * Generate range selector text, based on the state of a cell editor.
     */
    fun generateRangeSelectorText(editorState: CellEditorState): TextFieldValue
    /**
     * Make range selector text
     */
    fun generateRangeSelectorText(currentTextField:TextFieldValue,
                                  selectorId:CursorId?,
                                  cursorId:CursorId?):TextFieldValue
}
