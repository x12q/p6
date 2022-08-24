package com.qxdzbc.p6.app.action.workbook.new_worksheet

import com.qxdzbc.common.Rs
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.qxdzbc.common.error.ErrorReport

interface NewWorksheetAction {
    fun createNewWorksheetRs(request: CreateNewWorksheetRequest): Rs<CreateNewWorksheetResponse2, ErrorReport>
}
