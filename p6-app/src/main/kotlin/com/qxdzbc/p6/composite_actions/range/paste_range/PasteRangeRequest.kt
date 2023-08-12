package com.qxdzbc.p6.composite_actions.range.paste_range

import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWorkbookKeyAndWindowId
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

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
