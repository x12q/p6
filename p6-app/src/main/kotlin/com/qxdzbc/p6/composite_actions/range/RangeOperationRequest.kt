package com.qxdzbc.p6.composite_actions.range

import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWorkbookKeyAndWindowId
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.RangeProtos

open class RangeOperationRequest(
    val rangeId: RangeId,
    override val windowId: String?,
) : RequestWithWorkbookKeyAndWindowId, com.qxdzbc.p6.common.err.WithReportNavInfo {
    fun toProto(): RangeProtos.RangeOperationRequestProto {
        return RangeProtos.RangeOperationRequestProto.newBuilder()
            .setRangeId(this.rangeId.toProto()).apply {
                if (this@RangeOperationRequest.windowId != null) {
                    this.setWindowId(this@RangeOperationRequest.windowId)
                }
            }
            .build()
    }
    
    override val wbKey: WorkbookKey
        get() = rangeId.wbKey
}
