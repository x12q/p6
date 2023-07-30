package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.RequestWithWorkbookKeyAndWindowId
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

class CloseWorkbookRequest(
    override val wbKey: WorkbookKey,
    override val windowId:String? = null
): RequestWithWorkbookKeyAndWindowId
