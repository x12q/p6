package com.emeraldblast.p6.app.action.worksheet.delete_cell.rm

import com.emeraldblast.p6.app.action.worksheet.delete_cell.DeleteCellRequest
import com.emeraldblast.p6.app.action.worksheet.delete_cell.DeleteCellResponse

interface DeleteCellRM {
    fun deleteCell(request: DeleteCellRequest): DeleteCellResponse?
}
