package com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier

import com.qxdzbc.common.Rse

import com.qxdzbc.p6.app.document.workbook.Workbook
import javax.inject.Inject

class DeleteWorksheetApplierImp @Inject constructor(
    val internalApplier: DeleteWorksheetInternalApplier,
) : DeleteWorksheetApplier {
    override fun applyResRs(deletedWsName:String,rs: Rse<Workbook>): Rse<Unit> {
        return internalApplier.applyRs(deletedWsName,rs)
    }
}
