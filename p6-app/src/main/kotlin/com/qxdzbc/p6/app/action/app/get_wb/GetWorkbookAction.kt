package com.qxdzbc.p6.app.action.app.get_wb

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook

interface GetWorkbookAction {
    fun getWbRs(request:GetWorkbookRequest):Rse<Workbook>
}
