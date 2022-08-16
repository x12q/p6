package com.emeraldblast.p6.app.action.worksheet.rename_ws.applier

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface RenameWorksheetInternalApplier {
    fun apply(wbKey: WorkbookKey, oldName: String, newName: String)
}

