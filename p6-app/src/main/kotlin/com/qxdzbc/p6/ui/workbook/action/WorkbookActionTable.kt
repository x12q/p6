package com.qxdzbc.p6.ui.workbook.action

import com.qxdzbc.p6.composite_actions.workbook.WorkbookAction
import com.qxdzbc.p6.ui.worksheet.action.WorksheetActionTable

interface WorkbookActionTable {
    val wbAction: WorkbookAction
    val worksheetActionTable: WorksheetActionTable
}
