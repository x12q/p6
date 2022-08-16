package com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier

import com.emeraldblast.p6.app.common.Rse

import com.emeraldblast.p6.app.action.applier.BaseApplier
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.DeleteWorksheetResponse
import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import javax.inject.Inject

class DeleteWorksheetApplierImp @Inject constructor(
    val internalApplier: DeleteWorksheetInternalApplier,
) : DeleteWorksheetApplier {
    override fun applyResRs(deletedWsName:String,rs: Rse<Workbook>): Rse<Unit> {
        return internalApplier.applyRs(deletedWsName,rs)
    }
}
