package com.qxdzbc.p6.app.action.range

import com.qxdzbc.p6.app.common.err.WithReportNavInfo
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKeyAndWindowId
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.RangeProtos

open class RangeOperationRequest(
    val rangeId: RangeId,
    override val windowId: String?,
) : RequestToP6WithWorkbookKeyAndWindowId, WithReportNavInfo {
    fun toProto(): RangeProtos.RangeOperationRequestProto {
        return RangeProtos.RangeOperationRequestProto.newBuilder()
            .setRangeId(this.rangeId.toProto()).apply {
                if (this@RangeOperationRequest.windowId != null) {
                    this.setWindowId(this@RangeOperationRequest.windowId)
                }
            }
            .build()
    }

//    override fun toProtoBytes(): ByteString {
//        return this.toProto().toByteString()
//    }

    override val wbKey: WorkbookKey
        get() = rangeId.wbKey
}
