package com.qxdzbc.p6.app.action.app.load_wb.rm

import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse

interface LoadWorkbookRM {
    fun loadWb(request: LoadWorkbookRequest): LoadWorkbookResponse?
}
