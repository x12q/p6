package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId

/**
 * make text that is displayed on the cell editor UI.
 * When the cell editor is extracting cell address from the range selector, display text = current text + range-selector address
 */
interface MakeCellEditorDisplayTextAction {
    fun makeRangeSelectorText(editorState: CellEditorState): TextFieldValue
    fun makeRangeSelectorText(currentTextField:TextFieldValue,
                              selectorId:CursorStateId?,
                              cursorId:CursorStateId?):TextFieldValue
}
