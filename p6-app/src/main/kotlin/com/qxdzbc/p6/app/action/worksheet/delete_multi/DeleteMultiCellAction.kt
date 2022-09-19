package com.qxdzbc.p6.app.action.worksheet.delete_multi

import com.qxdzbc.p6.app.common.utils.RseNav

interface DeleteMultiCellAction {
    fun deleteMultiCellAtCursor(request:DeleteMultiAtCursorRequest): RseNav<RemoveMultiCellResponse>
    fun deleteMultiCell(request: RemoveMultiCellRequest): RseNav<RemoveMultiCellResponse>
}
