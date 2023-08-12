package com.qxdzbc.p6.composite_actions.workbook.create_new_ws

import com.qxdzbc.common.Rse

interface CreateNewWorksheetAction {
    fun createNewWorksheetRs(request: CreateNewWorksheetRequest): Rse<CreateNewWorksheetResponse>
}
