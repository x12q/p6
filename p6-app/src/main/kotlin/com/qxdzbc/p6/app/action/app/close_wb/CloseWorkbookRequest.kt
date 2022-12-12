package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKeyAndWindowId
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

class CloseWorkbookRequest(
    override val wbKey: WorkbookKey,
    override val windowId:String? = null
):RequestToP6WithWorkbookKeyAndWindowId {
//    override fun toProtoBytes(): ByteString {
//        throw UnsupportedOperationException()
//    }
}
