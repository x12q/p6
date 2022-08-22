package com.qxdzbc.p6.ui.window.focus_state

import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState

/**
 * Focus state in a window
 */
interface WindowFocusState: CursorFocusState {
    fun freeAllFocus():WindowFocusState

    /**
     * restore to default focus state
     */
    fun restoreDefault():WindowFocusState

    fun focusOnCursor():WindowFocusState
    fun freeFocusOnCursor():WindowFocusState

    fun focusOnEditor():WindowFocusState
    fun freeFocusOnEditor():WindowFocusState
}
