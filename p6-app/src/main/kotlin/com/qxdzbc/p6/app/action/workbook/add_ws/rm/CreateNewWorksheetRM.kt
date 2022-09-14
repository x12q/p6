package com.qxdzbc.p6.app.action.workbook.add_ws.rm

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse

interface CreateNewWorksheetRM {
    fun makeRequest(req: AddWorksheetRequest): RseNav<AddWorksheetResponse>
}
