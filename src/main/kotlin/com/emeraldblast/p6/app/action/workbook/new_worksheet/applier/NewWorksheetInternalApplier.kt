package com.emeraldblast.p6.app.action.workbook.new_worksheet.applier

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface NewWorksheetInternalApplier {
    fun apply(
        newWb:Workbook,
        newWorksheetName: String,
    ):Rse<Unit>
}

