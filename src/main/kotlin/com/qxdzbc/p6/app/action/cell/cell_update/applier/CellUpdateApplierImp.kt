package com.qxdzbc.p6.app.action.cell.cell_update.applier

import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateResponse
import javax.inject.Inject

class CellUpdateApplierImp @Inject constructor(
    private val wbUpdateApplier: WorkbookUpdateCommonApplier,
): CellUpdateApplier {
    override fun applyRes(res: CellUpdateResponse?) {
        wbUpdateApplier.apply(res)
    }
}
