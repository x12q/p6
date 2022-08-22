package com.qxdzbc.p6.app.action.app.save_wb.applier

import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface SaveWorkbookInternalApplier{
    fun apply(workbookKey: WorkbookKey, path:String)
}

