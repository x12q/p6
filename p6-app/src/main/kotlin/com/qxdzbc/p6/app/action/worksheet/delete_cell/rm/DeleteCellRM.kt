package com.qxdzbc.p6.app.action.worksheet.delete_cell.rm

import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellRequest
import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellResponse

interface DeleteCellRM {
    fun deleteCell(request: DeleteCellRequest): DeleteCellResponse?
}
