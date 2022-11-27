package com.qxdzbc.p6.app.action.cell_editor.handle_keyboard_event

import com.qxdzbc.p6.app.common.key_event.P6KeyEvent

interface HandleKeyboardEventOnCellEditorAction{
    /**
     * This function is for handling keyboard events that a cell editor receives. This overrides certain actions/events of a typical text editor, such as:
     *  - Enter key = run the formula instead of inserting new lines
     *  - Alt+Enter = insert new line
     *  - Escape key = close the cell editor
     *  - pass key event to range selector if possible
     */
    fun handleKeyboardEvent(keyEvent: P6KeyEvent): Boolean
}
