package com.emeraldblast.p6.app.action.app.create_new_wb.applier

import com.emeraldblast.p6.app.document.workbook.Workbook

interface CreateNewWorkbookInternalApplier {
    fun apply(workbook: Workbook?, windowId: String?)
}
