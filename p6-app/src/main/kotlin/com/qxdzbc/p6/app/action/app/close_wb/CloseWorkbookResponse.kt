package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWith_WindowId_WbKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class CloseWorkbookResponse(
    override val wbKey: WorkbookKey?,
    override val windowId: String?,
    override val errorReport: ErrorReport?,
): ResponseWith_WindowId_WbKey {
    override val isError: Boolean
        get() = errorReport!=null
}
