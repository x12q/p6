package com.qxdzbc.p6.app.action.range.range_to_clipboard

import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeId.Companion.toModel
import com.qxdzbc.p6.app.action.common_data_structure.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey2
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.RangeProtos
import com.google.protobuf.ByteString

data class RangeToClipboardResponse(
    override val errorIndicator: ErrorIndicator,
    val rangeId: RangeId,
    override val windowId:String?
): ResponseWithWindowIdAndWorkbookKey2 {
    companion object{
        fun fromProtoBytes(data:ByteString): RangeToClipboardResponse {
            val proto=RangeProtos.RangeToClipboardResponseProto.newBuilder().mergeFrom(data)
            return RangeToClipboardResponse(
                errorIndicator = proto.errorIndicator.toModel(),
                rangeId = proto.rangeId.toModel(),
                windowId = if(proto.hasWindowId()) proto.windowId else null
            )
        }
    }

    override fun isLegal(): Boolean {
        return true
    }
    override val wbKey: WorkbookKey?
        get() = rangeId.wbKey
}
