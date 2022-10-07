package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest

/**
 * TODO need an alternative that use St key to improve lookup performance
 */
interface UpdateCellAction {
    fun updateCell2(request: CellUpdateRequest, publishError:Boolean = true):Rse<Unit>
}
