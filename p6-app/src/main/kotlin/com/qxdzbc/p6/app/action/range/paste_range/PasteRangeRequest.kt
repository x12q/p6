package com.qxdzbc.p6.app.action.range.paste_range

import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.communication.res_req_template.request.RequestWithWorkbookKeyAndWindowId
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

class PasteRangeRequest(
    val rangeId: RangeId,
    override val windowId: String?
): RequestWithWorkbookKeyAndWindowId {
//    override fun toProtoBytes(): ByteString {
//        return ByteString.EMPTY
//    }

    val wsName:String = rangeId.wsName

    override val wbKey: WorkbookKey
        get() = rangeId.wbKey
}
