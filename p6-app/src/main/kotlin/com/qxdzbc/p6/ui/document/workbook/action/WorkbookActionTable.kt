package com.qxdzbc.p6.ui.document.workbook.action

import com.qxdzbc.p6.app.action.workbook.WorkbookAction
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable

interface WorkbookActionTable {
    val wbAction: WorkbookAction
    val worksheetActionTable: WorksheetActionTable
}
