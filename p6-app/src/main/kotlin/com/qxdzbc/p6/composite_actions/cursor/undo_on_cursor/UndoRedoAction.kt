package com.qxdzbc.p6.composite_actions.cursor.undo_on_cursor

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt

interface UndoRedoAction{
    /**
     * Perform undo on a worksheet at [wbwsSt]
     */
    fun undoOnWorksheet(wbwsSt: WbWsSt)

    /**
     * Perform undo on a worksheet at [wbwsSt]
     */
    fun redoOnWorksheet(wbwsSt: WbWsSt)
}
