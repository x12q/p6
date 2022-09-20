package com.qxdzbc.p6.ui.document.cell.action

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2

interface UpdateCellAction {
    @Deprecated("dont use, use updateCell2 instead")
    fun updateCell(request: CellUpdateRequest):Rse<Unit>
    fun updateCell2(request: CellUpdateRequest2, publishError:Boolean = true):Rse<Unit>
}
