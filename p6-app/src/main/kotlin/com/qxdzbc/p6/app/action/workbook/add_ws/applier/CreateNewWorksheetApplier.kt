package com.qxdzbc.p6.app.action.workbook.add_ws.applier

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse

interface CreateNewWorksheetApplier {
    fun applyRs(res: RseNav<AddWorksheetResponse>): RseNav<AddWorksheetResponse>
}
