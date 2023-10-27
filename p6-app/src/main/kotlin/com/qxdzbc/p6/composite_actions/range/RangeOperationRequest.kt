package com.qxdzbc.p6.composite_actions.range

import com.qxdzbc.p6.common.err.WithReportNavInfo
import com.qxdzbc.p6.rpc.communication.res_req_template.request.RequestWithWorkbookKeyAndWindowId
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.RangeProtos

/**
 * This is common input for ws range operation.
 *
 * This contains:
 * - a [RangeId] to identify the target range,
 * - [windowId] to identify the relevant window. Often the [windowId] is for error reporting purpose. It's up to the action on how to use the [windowId].
 *
 * This class can also construct a proto object containing its information.
 */
open class RangeOperationRequest(
    val rangeId: RangeId,
    override val windowId: String?,
) : RequestWithWorkbookKeyAndWindowId, WithReportNavInfo {


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
