package com.emeraldblast.p6.app.action.app.load_wb.rm

import com.emeraldblast.p6.app.action.app.load_wb.LoadWorkbookRequest
import com.emeraldblast.p6.app.action.app.load_wb.LoadWorkbookResponse

interface LoadWorkbookRM {
    fun loadWb(request: LoadWorkbookRequest): LoadWorkbookResponse?
}
