package com.qxdzbc.p6.ui.document.workbook.action

import com.qxdzbc.p6.app.action.workbook.WorkbookAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class WorkbookActionTableImp @Inject constructor(
    override val wbAction: WorkbookAction,
    override val worksheetActionTable: WorksheetActionTable
) : WorkbookActionTable {
}
