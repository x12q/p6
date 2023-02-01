package com.qxdzbc.p6.app.action.worksheet.delete_multi.rm

import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAtCursorRequest
import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellResponse
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellRequest
import com.qxdzbc.p6.app.common.utils.RseNav

interface DeleteMultiRM {
    fun deleteMultiCellAtCursor(request: DeleteMultiCellAtCursorRequest): RseNav<RemoveMultiCellResponse>
    fun deleteMultiCell(request: DeleteMultiCellRequest):RseNav<RemoveMultiCellResponse>
}
