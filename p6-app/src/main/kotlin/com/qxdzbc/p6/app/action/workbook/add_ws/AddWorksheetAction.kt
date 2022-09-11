package com.qxdzbc.p6.app.action.workbook.add_ws

import com.qxdzbc.common.Rse

interface AddWorksheetAction {
    fun addWorksheetRs(request: AddWorksheetRequest): Rse<AddWorksheetResponse>
}
