package com.qxdzbc.p6.app.action.worksheet.delete_multi

import com.qxdzbc.p6.app.common.utils.RseNav

interface DeleteMultiCellAction {
    fun deleteMultiCellAtCursor(request:DeleteMultiAtCursorRequest): RseNav<DeleteMultiResponse>
    fun deleteMultiCell(request: DeleteMultiRequest): RseNav<DeleteMultiResponse>
}
