package com.qxdzbc.p6.app.action.workbook.add_ws

import com.qxdzbc.common.Rse

interface CreateNewWorksheetAction {
    fun createNewWorksheetRs(request: AddWorksheetRequest): Rse<AddWorksheetResponse>
}
