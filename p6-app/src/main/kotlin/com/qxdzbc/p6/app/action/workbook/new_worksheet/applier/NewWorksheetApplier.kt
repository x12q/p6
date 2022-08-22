package com.qxdzbc.p6.app.action.workbook.new_worksheet.applier

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2

interface NewWorksheetApplier {
    fun applyRes2(rs: RseNav<CreateNewWorksheetResponse2>): RseNav<CreateNewWorksheetResponse2>
}
