package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId

/**
 * make texts for on the cell editor UI.
 */
interface MakeCellEditorTextAction {
    /**
     * Make range selector text, based on the state of the input editor.
     */
    fun makeRangeSelectorText(editorState: CellEditorState): TextFieldValue
    /**
     * Make range selector text, based on the state of the input text.
     */
    fun makeRangeSelectorText(currentTextField:TextFieldValue,
                              selectorId:CursorStateId?,
                              cursorId:CursorStateId?):TextFieldValue
}
