package com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

/**
 * An action that is run when the cell cursor move
 */
interface CommonReactionOnCursorChanged {
    /**
     * Invoke a set of common effects when a cursor changed. See implementation for detail of what effects are emitted.
     */
    fun onCursorChanged(cursorId:WbWsSt)
}


