package com.emeraldblast.p6.app.action.workbook.new_worksheet.applier

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface NewWorksheetInternalApplier {
    @Deprecated("old arc")
    fun apply(
        workbookKey: WorkbookKey,
        newWorksheetName: String
    )
    fun apply(
        newWb:Workbook,
        newWorksheetName: String,
    )
}

