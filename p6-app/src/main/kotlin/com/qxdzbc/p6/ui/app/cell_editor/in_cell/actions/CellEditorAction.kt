package com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.key_event.PKeyEvent
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction

interface CellEditorAction : MakeCellEditorDisplayTextAction, OpenCellEditorAction {

    /**
     * Focus on the cell editor
     */
    fun focus()

    /**
     * run the current formula inside the cell editor if there's a formula. Otherwise, parse and save the current value in the editor into the target cell
     */
    fun runFormulaOrSaveValueToCell()

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

