package com.qxdzbc.p6.app.action.workbook.add_ws

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.rpc.document.workbook.msg.AddWorksheetRequest
import com.qxdzbc.p6.rpc.document.workbook.msg.AddWorksheetResponse

interface AddWorksheetAction {
    fun addWorksheetRs(request: AddWorksheetRequest): Rse<AddWorksheetResponse>
}
