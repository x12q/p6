package com.qxdzbc.p6.app.action.app.close_wb.rm

import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse

interface CloseWorkbookRM {
    fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse
}
