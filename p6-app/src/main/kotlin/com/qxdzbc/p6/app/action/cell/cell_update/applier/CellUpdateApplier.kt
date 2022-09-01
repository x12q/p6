package com.qxdzbc.p6.app.action.cell.cell_update.applier

import com.qxdzbc.p6.app.action.applier.ResApplier
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateResponse

interface CellUpdateApplier  {
    fun applyRes(res: CellUpdateResponse?)
}

