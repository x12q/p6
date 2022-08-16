package com.emeraldblast.p6.app.action.app.close_wb.rm

import com.emeraldblast.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.emeraldblast.p6.app.action.app.close_wb.CloseWorkbookResponse
import javax.inject.Inject

class FakeCloseWorkbookRM @Inject constructor(): CloseWorkbookRM {
    override fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse? {
        return CloseWorkbookResponse(
            isError = false,
            wbKey = request.wbKey,
            windowId = request.windowId,
            errorReport = null
        )
    }
}
