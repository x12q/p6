package com.qxdzbc.p6.app.action.app.load_wb

import com.google.protobuf.ByteString
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.AppProtos

data class LoadWorkbookResponse(
    override val errorReport: ErrorReport?,
    override val windowId:String?,
    val workbook: Workbook?
) : ResponseWithWindowIdAndWorkbookKey {

//    companion object {
//        fun fromProtoBytes(
//            data: ByteString,
//        ): LoadWorkbookResponse {
//            val proto: AppProtos.LoadWorkbookResponseProto =
//                AppProtos.LoadWorkbookResponseProto.newBuilder().mergeFrom(data).build()
//            return LoadWorkbookResponse(
//                errorReport = if (proto.hasErrorReport()) proto.errorReport.toModel() else null,
//                wbKey = if (proto.hasWbKey()) proto.wbKey.toModel() else null,
//            )
//        }
//    }

    override val isError: Boolean
        get() = errorReport != null
    override val wbKey: WorkbookKey?
        get() = workbook?.key

    override fun isLegal(): Boolean {
        return true
    }
}


