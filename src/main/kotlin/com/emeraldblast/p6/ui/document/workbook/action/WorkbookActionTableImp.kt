package com.emeraldblast.p6.ui.document.workbook.action

import com.emeraldblast.p6.app.action.workbook.WorkbookAction
import com.emeraldblast.p6.ui.document.worksheet.action.WorksheetActionTable
import javax.inject.Inject

class WorkbookActionTableImp @Inject constructor(
    override val wbAction: WorkbookAction,
    override val worksheetActionTable: WorksheetActionTable
) : WorkbookActionTable {
}
