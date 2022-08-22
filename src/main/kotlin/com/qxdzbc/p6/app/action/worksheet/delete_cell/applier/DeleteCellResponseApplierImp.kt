package com.qxdzbc.p6.app.action.worksheet.delete_cell.applier

import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellResponse
import javax.inject.Inject

class DeleteCellResponseApplierImp @Inject constructor(
    private val i: DeleteCellResponseInternalApplier,
    private val baseApplier: BaseApplier
) : DeleteCellResponseApplier {
    override fun applyRes(res: DeleteCellResponse?) {
        baseApplier.applyRes(res) {
            i.apply(it.wbKey, it.wsName, it.cellAddress, it.newWorkbook)
        }
    }
}
