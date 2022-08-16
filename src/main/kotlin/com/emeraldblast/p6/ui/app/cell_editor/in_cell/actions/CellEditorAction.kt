package com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions

import androidx.compose.ui.text.input.TextFieldValue
import com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.emeraldblast.p6.app.common.utils.PKeyEvent
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState

interface CellEditorAction : MakeCellEditorDisplayText {
    fun focus()
    fun runFormula()
    fun closeEditor()
    fun onTextChange(newText: String, )
    fun onTextFieldChange(newTextField: TextFieldValue, )
    fun handleKeyboardEvent(keyEvent: PKeyEvent, ): Boolean
}

