package com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.ui.text.input.TextFieldValue
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState


fun interface MakeCellEditorDisplayText {
    fun makeRangeSelectorText(editorState: CellEditorState): TextFieldValue
}
