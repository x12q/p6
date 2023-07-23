package com.qxdzbc.p6.ui.app.cell_editor.actions

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.P6ExperimentalApi
import com.qxdzbc.p6.app.action.cell_editor.close_cell_editor.CloseCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.handle_keyboard_event.HandleKeyboardEventOnCellEditor
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.run_formula.RunFormulaOrSaveValueToCellAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.GenerateCellEditorTextAction

interface CellEditorAction : GenerateCellEditorTextAction, OpenCellEditorAction, CycleFormulaLockStateAction,
    HandleKeyboardEventOnCellEditor, CloseCellEditorAction, RunFormulaOrSaveValueToCellAction {

    /**
     * Focus on the cell editor
     */
    fun focusOnCellEditor()
    fun freeFocusOnCellEditor()
    fun setCellEditorFocus(i:Boolean)

    /**
     * **For testing only.**
     * Be careful when using this function. It directly updates the text content and may erase all the text formats. Should be used for testing only. Even so, be extra careful when use this in tests. Use [changeRawText] in the app.
     */
    @P6ExperimentalApi("For testing only")
    fun changeRawText(newText: String)

    /**
     * This function does the following:
     *  - update the text field displayed by the cell editor
     *  - point the new text to the correct location which could be either the current text, or the temp text, depending on the current state of the state editor. If the range selector is activated, the new text will be stored in the temp text, and only when the range selector is deactivated, the temp text is moved to the current text.
     *  - update the internal parse tree of cell editor
     */
    fun changeTextField(newTextField: TextFieldValue)
}

