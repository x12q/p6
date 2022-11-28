package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey


interface CloseWorkbookAction {
    fun closeWb(request: CloseWorkbookRequest):CloseWorkbookResponse
    fun closeWb(wbKeySt:St<WorkbookKey>)
}
