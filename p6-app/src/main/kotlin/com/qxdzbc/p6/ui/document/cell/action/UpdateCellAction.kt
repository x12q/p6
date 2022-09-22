package com.qxdzbc.p6.ui.document.cell.action

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest

interface UpdateCellAction {
    fun updateCell2(request: CellUpdateRequest, publishError:Boolean = true):Rse<Unit>
}
