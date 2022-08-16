package com.emeraldblast.p6.app.action.app.load_wb.applier

import com.emeraldblast.p6.app.document.workbook.Workbook

interface LoadWorkbookInternalApplier {
    fun apply(
        windowId: String?,
        workbook: Workbook?
    )
}

