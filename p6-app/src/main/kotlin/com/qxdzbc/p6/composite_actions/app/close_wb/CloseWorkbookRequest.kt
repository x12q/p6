package com.qxdzbc.p6.composite_actions.app.close_wb

import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWorkbookKeyAndWindowId
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

class CloseWorkbookRequest(
    override val wbKey: WorkbookKey,
    override val windowId:String? = null
): RequestWithWorkbookKeyAndWindowId
