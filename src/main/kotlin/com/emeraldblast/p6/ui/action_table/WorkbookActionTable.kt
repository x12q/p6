package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.action.workbook.WorkbookAction

interface WorkbookActionTable {
    fun getWbAction(): WorkbookAction
    val worksheetActionTable:WorksheetActionTable
}
