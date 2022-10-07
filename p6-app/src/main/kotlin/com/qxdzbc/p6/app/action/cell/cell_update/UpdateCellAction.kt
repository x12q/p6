package com.qxdzbc.p6.app.action.cell.cell_update

import com.qxdzbc.common.Rse

/**
 * TODO need an alternative that use St key to improve lookup performance
 */
interface UpdateCellAction {
    fun updateCellDM(request: CellUpdateRequestDM, publishError:Boolean = true):Rse<Unit>
    fun updateCell(request: CellUpdateRequest, publishError:Boolean = true):Rse<Unit>
}
