package com.emeraldblast.p6.ui.document.workbook.action

import com.emeraldblast.p6.app.action.workbook.WorkbookAction
import com.emeraldblast.p6.ui.document.worksheet.action.WorksheetActionTable

interface WorkbookActionTable {
    val wbAction: WorkbookAction
    val worksheetActionTable: WorksheetActionTable
}
