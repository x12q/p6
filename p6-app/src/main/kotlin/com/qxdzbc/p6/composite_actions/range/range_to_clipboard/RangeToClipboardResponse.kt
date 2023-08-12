package com.qxdzbc.p6.composite_actions.range.range_to_clipboard

import com.qxdzbc.p6.composite_actions.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.composite_actions.range.RangeIdDM.Companion.toModel
import com.qxdzbc.p6.composite_actions.common_data_structure.toModel
import com.qxdzbc.p6.rpc.communication.res_req_template.response.ResponseWith_WindowId_WbKey_ErrorReport
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.RangeProtos
import com.google.protobuf.ByteString
import com.qxdzbc.p6.composite_actions.range.RangeId

data class RangeToClipboardResponse(
    override val errorIndicator: ErrorIndicator,
    val rangeId: RangeId,
    override val windowId:String?
): ResponseWith_WindowId_WbKey_ErrorReport {
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

    override val wbKey: WorkbookKey?
        get() = rangeId.wbKey
}
