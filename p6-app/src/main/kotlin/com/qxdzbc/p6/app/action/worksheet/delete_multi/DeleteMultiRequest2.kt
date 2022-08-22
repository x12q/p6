package com.qxdzbc.p6.app.action.worksheet.delete_multi

import com.qxdzbc.p6.app.action.WbWsRequest
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

class DeleteMultiRequest2(
    wbKey: WorkbookKey,
    wsName: String,
    windowId: String? = null,
    val clearFormat: Boolean = false
) :
    WbWsRequest(wbKey, wsName, windowId) {
}
