package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.qxdzbc.common.Rse

interface MultiCellUpdateAction {
    fun updateMultiCell(request:MultiCellUpdateRequestDM, publishErr:Boolean = true):Rse<Unit>
}
