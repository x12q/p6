package com.qxdzbc.p6.app.action.cursor.undo_on_cursor

import com.qxdzbc.p6.app.action.common_data_structure.WbWs

interface UndoOnCursorAction{
    /**
     * Perform undo on a cursor
     */
    fun undoOnCursor(wbws: WbWs)
}
