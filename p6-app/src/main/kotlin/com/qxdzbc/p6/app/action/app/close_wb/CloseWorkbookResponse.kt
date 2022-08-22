package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.proto.AppEventProtos.CloseWorkbookResponseProto
import com.google.protobuf.ByteString

data class CloseWorkbookResponse(
    override val isError: Boolean,
    override val wbKey: WorkbookKey?,
    override val windowId: String?,
    override val errorReport: ErrorReport?,
): ResponseWithWindowIdAndWorkbookKey {
    companion object {
        fun fromProtoBytes(data:ByteString): CloseWorkbookResponse {
            val proto = CloseWorkbookResponseProto.newBuilder().mergeFrom(data).build()
            return CloseWorkbookResponse(
                isError = proto.isError,
                wbKey = if(proto.hasWorkbookKey())proto.workbookKey.toModel() else null,
                windowId = if (proto.hasWindowId()) proto.windowId else null,
                errorReport = if(proto.hasErrorReport()) proto.errorReport.toModel() else null
            )
        }
    }

    override fun isLegal():Boolean{
        if(!isError){
            return errorReport == null
        }
        return true
    }
}
