package com.qxdzbc.p6.app.action.cursor.propagate_move_cursor_event

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId

interface PropagateMoveCursorEvent {
    fun propagateMoveCursorEvent(cursorId:CursorId)
}
