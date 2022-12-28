package com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor

import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MoveSliderAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

/**
 * An action that is run when the cell cursor move
 */
interface OnCursorChangeEventReactor {
    /**
     * Invoke a set of common affect when a cursor changed (not applicable in certain cases such as when pressing page up, page down)
     */
    fun onCursorChanged(cursorId:CursorId)
    fun onCursorChanged(cursorState: CursorState)
    fun updateFormatIndicator(cursorState: CursorState)
}
