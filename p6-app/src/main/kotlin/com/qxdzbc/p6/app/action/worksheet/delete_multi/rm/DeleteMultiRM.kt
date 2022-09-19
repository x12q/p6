package com.qxdzbc.p6.app.action.worksheet.delete_multi.rm

import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiAtCursorRequest
import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellResponse
import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellRequest
import com.qxdzbc.p6.app.common.utils.RseNav

interface DeleteMultiRM {
    fun deleteMultiCellAtCursor(request: DeleteMultiAtCursorRequest): RseNav<RemoveMultiCellResponse>
    fun deleteMultiCell(request: RemoveMultiCellRequest):RseNav<RemoveMultiCellResponse>
}
