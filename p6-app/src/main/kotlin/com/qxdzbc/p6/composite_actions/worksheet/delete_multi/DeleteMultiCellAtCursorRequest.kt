package com.qxdzbc.p6.composite_actions.worksheet.delete_multi

import com.qxdzbc.p6.composite_actions.WbWsRequest
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

class DeleteMultiCellAtCursorRequest(
    wbKey: WorkbookKey,
    wsName: String,
    windowId: String? = null,
    val clearFormat: Boolean = false
) :
    WbWsRequest(wbKey, wsName, windowId) {
}
