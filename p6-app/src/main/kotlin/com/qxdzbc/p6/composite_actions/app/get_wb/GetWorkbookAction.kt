package com.qxdzbc.p6.composite_actions.app.get_wb

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.document_data_layer.workbook.Workbook

interface GetWorkbookAction {
    fun getWbRs(request:GetWorkbookRequest):Rse<Workbook>
}
