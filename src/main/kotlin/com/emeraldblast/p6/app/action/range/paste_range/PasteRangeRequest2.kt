package com.emeraldblast.p6.app.action.range.paste_range

import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKeyAndWindowId
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.google.protobuf.ByteString

class PasteRangeRequest2(
    val rangeId: RangeId,
    override val windowId: String?
): RequestToP6WithWorkbookKeyAndWindowId {
    override fun toProtoBytes(): ByteString {
        return ByteString.EMPTY
    }

    val wsName:String = rangeId.wsName

    override val wbKey: WorkbookKey
        get() = rangeId.wbKey
}
