package com.qxdzbc.p6.app.action.worksheet.delete_multi

import com.qxdzbc.p6.app.common.utils.RseNav

/**
 * Delete data and/or format multiple cells
 */
interface DeleteMultiCellAction {
    /**
     * Delete data of multiple cells at cursor
     */
    fun deleteDataOfMultiCellAtCursor(request:DeleteMultiCellAtCursorRequest): RseNav<RemoveMultiCellResponse>

    /**
     * Delete data of multiple cell
     */
    fun deleteDataOfMultiCell(request: DeleteMultiCellRequest, undoable:Boolean): RseNav<RemoveMultiCellResponse>
}
