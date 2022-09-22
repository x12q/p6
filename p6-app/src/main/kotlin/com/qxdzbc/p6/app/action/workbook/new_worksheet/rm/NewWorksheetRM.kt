package com.qxdzbc.p6.app.action.workbook.new_worksheet.rm

import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.qxdzbc.p6.app.common.utils.RseNav

interface NewWorksheetRM {
    fun newWorksheet(request: CreateNewWorksheetRequest): RseNav<CreateNewWorksheetResponse>
}

