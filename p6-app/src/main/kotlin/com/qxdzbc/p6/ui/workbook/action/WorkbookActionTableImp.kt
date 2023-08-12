package com.qxdzbc.p6.ui.workbook.action

import com.qxdzbc.p6.composite_actions.workbook.WorkbookAction
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.worksheet.action.WorksheetActionTable
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class WorkbookActionTableImp @Inject constructor(
    override val wbAction: WorkbookAction,
    override val worksheetActionTable: WorksheetActionTable
) : WorkbookActionTable {
}
