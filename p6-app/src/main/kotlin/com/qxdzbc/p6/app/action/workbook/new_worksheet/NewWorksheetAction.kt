package com.qxdzbc.p6.app.action.workbook.new_worksheet

import com.qxdzbc.p6.app.common.utils.Rs
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.qxdzbc.p6.common.exception.error.ErrorReport

interface NewWorksheetAction {
    fun createNewWorksheetRs(request: CreateNewWorksheetRequest): Rs<CreateNewWorksheetResponse2, ErrorReport>
}
