package com.qxdzbc.p6.ui.window.focus_state

import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorFocusStatePerWindow

/**
 * contain focus state of views in a window. Each window has one of this.
 */
interface WindowFocusState: CursorFocusStatePerWindow {
    fun freeAllFocus():WindowFocusState

    /**
     * restore to default focus state
     */
    fun restoreDefault():WindowFocusState

    override fun setCursorFocus(i: Boolean): WindowFocusState

    override fun focusOnCursor(): WindowFocusState

    override fun freeFocusOnCursor(): WindowFocusState

    override fun setCellEditorFocus(i: Boolean): WindowFocusState

    override fun focusOnEditor(): WindowFocusState

    override fun freeFocusOnEditor(): WindowFocusState
}
