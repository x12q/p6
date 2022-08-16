package com.emeraldblast.p6.app.action.app.close_wb.applier

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface CloseWorkbookInternalApplier{
    fun apply( workbookKey: WorkbookKey?, windowId: String?)
}

