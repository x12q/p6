package com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions

import androidx.compose.ui.text.input.TextFieldValue
import com.emeraldblast.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.emeraldblast.p6.app.common.utils.PKeyEvent

interface CellEditorAction : MakeCellEditorDisplayText, OpenCellEditorAction {

    /**
     * Focus on the cell editor
     */
    fun focus()

    /**
     * run the current formula inside the cell editor
     */
    fun runFormula()

    /**
     * close the cell editor
     */
    fun closeEditor()

    /**
     * update the text in the cell editor
     */
    fun updateText(newText: String)

    /**
     * update the text in the cell editor
     */
    fun updateTextField(newTextField: TextFieldValue)

    /**
     * handle keyboard event
     */
    fun handleKeyboardEvent(keyEvent: PKeyEvent): Boolean
}

