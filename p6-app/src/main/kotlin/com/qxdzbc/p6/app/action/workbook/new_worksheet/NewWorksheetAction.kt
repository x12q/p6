package com.qxdzbc.p6.app.action.workbook.new_worksheet

import com.qxdzbc.common.Rse

interface NewWorksheetAction {
    fun createNewWorksheetRs(request: CreateNewWorksheetRequest, publishError:Boolean = true): Rse<CreateNewWorksheetResponse>
}
