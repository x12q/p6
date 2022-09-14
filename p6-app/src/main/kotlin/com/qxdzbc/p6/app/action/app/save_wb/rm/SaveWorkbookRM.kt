package com.qxdzbc.p6.app.action.app.save_wb.rm

import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookRequest
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookResponse

interface SaveWorkbookRM{
    fun makeRequest(request: SaveWorkbookRequest): SaveWorkbookResponse
}
