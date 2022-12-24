package com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor

import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId

/**
 * An action that is run when the cell cursor move
 */
interface OnCursorChangeEventReactor {
    fun onCursorChanged(cursorId:CursorId)
}
