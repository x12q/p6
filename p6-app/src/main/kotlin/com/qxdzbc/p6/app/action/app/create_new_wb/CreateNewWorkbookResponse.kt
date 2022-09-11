package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.communication.res_req_template.response.Response
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.AppProtos.CreateNewWorkbookResponseProto

data class CreateNewWorkbookResponse(
    override val errorReport: ErrorReport?,
    val wb: Workbook?,
    override val windowId: String?
) : ResponseWithWindowIdAndWorkbookKey {
    fun toProto():CreateNewWorkbookResponseProto{
        return CreateNewWorkbookResponseProto.newBuilder()
            .setErrorReport(errorReport?.toProto())
            .setWbKey(wb?.key?.toProto())
            .setWindowId(windowId)
            .build()
    }
    //    companion object {
//        fun fromProtoBytes(
//            data: ByteString,
//        ): CreateNewWorkbookResponse {
//            val proto = AppProtos.CreateNewWorkbookResponseProto.newBuilder().mergeFrom(data).build()
//            return CreateNewWorkbookResponse(
//                errorReport = if (proto.hasErrorReport()) proto.errorReport.toModel() else null,
//                wbKey = if (proto.hasWbKey()) proto.wbKey.toModel() else null,
//                windowId = if (proto.hasWindowId()) proto.windowId else null
//            )
//        }
//    }
    override val isError: Boolean
        get() = errorReport!=null
    override val wbKey: WorkbookKey?
        get() = wb?.key

    override fun isLegal(): Boolean {
        return true
    }
}
