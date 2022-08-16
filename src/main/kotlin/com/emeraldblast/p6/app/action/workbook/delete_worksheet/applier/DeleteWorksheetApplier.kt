package com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier

import com.emeraldblast.p6.app.common.Rse

import com.emeraldblast.p6.app.action.workbook.delete_worksheet.DeleteWorksheetResponse
import com.emeraldblast.p6.app.document.workbook.Workbook

interface DeleteWorksheetApplier {
    fun applyResRs(deletedWsName:String,rs: Rse<Workbook>):Rse<Unit>
}

