package com.qxdzbc.p6.app.action.worksheet.delete_multi.rm

import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiAtCursorRequest
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiResponse
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiRequest
import com.qxdzbc.p6.app.common.utils.RseNav

interface DeleteMultiRM {
    fun deleteMultiCellAtCursor(request: DeleteMultiAtCursorRequest): RseNav<DeleteMultiResponse>
    fun deleteMultiCell(request: DeleteMultiRequest):RseNav<DeleteMultiResponse>
}
