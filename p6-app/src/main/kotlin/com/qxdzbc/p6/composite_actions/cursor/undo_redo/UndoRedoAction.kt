package com.qxdzbc.p6.composite_actions.cursor.undo_redo

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt

/**
 * Perform undo-redo.
 */
interface UndoRedoAction{
    /**
     * Perform undo on a worksheet at [wbwsSt]. This uses the command stack of such worksheet to decide what to undo.
     */
    fun undoOnWorksheet(wbwsSt: WbWsSt)

    /**
     * Perform undo on a worksheet at [wbwsSt]. This uses the command stack of such worksheet to decide what to redo.
     */
    fun redoOnWorksheet(wbwsSt: WbWsSt)
}
