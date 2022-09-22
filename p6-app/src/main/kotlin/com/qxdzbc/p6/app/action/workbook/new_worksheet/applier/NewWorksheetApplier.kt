package com.qxdzbc.p6.app.action.workbook.new_worksheet.applier

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse

interface NewWorksheetApplier {
    fun applyRes2(rs: RseNav<CreateNewWorksheetResponse>): RseNav<CreateNewWorksheetResponse>
}
