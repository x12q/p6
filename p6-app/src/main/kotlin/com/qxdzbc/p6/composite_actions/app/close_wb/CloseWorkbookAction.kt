package com.qxdzbc.p6.composite_actions.app.close_wb

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey


interface CloseWorkbookAction {
    fun closeWb(request: CloseWorkbookRequest):CloseWorkbookResponse
    fun closeWb(wbKeySt:St<WorkbookKey>)
}
