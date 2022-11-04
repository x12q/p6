package com.qxdzbc.p6.ui.app.cell_editor.actions

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.key_event.MKeyEvent
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent

interface CellEditorAction : MakeCellEditorDisplayTextAction, OpenCellEditorAction, CycleFormulaLockStateAction {

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
     * **For testing only.**
     * Be careful when using this function. It directly updates the text content and may erase all the text formats. Should be use for testing only. Even so, be extra careful when use this in tests. Use [changeText] in the app.
     */
    fun changeText(newText: String)

    /**
     * This function does the following:
     *  - update the text field displayed by the cell editor
     *  - point the new text to the correct location which could be either the current text, or the temp text, depending on the current state of the state editor. If the range selector is activated, the new text will be stored in the temp text, and only when the range selector is deactivated, the temp text is moved to the current text.
     *  - update the internal parse tree of cell editor
     */
    fun changeTextField(newTextField: TextFieldValue)

    /**
     * This function is for handling keyboard events that a cell editor receives. This overrides certain actions/events of a typical text editor, such as:
     *  - Enter key = run the formula instead of inserting new lines
     *  - Alt+Enter = insert new line
     *  - Escape key = close the cell editor
     *  - pass key event to range selector if possible
     */
    fun handleKeyboardEvent(keyEvent: P6KeyEvent): Boolean
}

