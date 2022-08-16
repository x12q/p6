package com.emeraldblast.p6.app.action.app.close_wb.rm

import com.emeraldblast.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.emeraldblast.p6.app.action.app.close_wb.CloseWorkbookResponse

interface CloseWorkbookRM {
    fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse?
}
