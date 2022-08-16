package com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier

import com.emeraldblast.p6.app.common.Rse

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface DeleteWorksheetInternalApplier {
    fun applyRs(targetSheetName:String,rs: Rse<Workbook>):Rse<Unit>
}

