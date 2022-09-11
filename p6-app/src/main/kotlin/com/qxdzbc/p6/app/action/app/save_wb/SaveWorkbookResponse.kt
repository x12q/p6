package com.qxdzbc.p6.app.action.app.save_wb

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.common.error.ErrorReport
import com.google.protobuf.ByteString
import com.qxdzbc.p6.proto.AppProtos

class SaveWorkbookResponse(
    override val errorReport: ErrorReport?,
    override val wbKey:WorkbookKey,
    val path:String,
) :ResponseWithWorkbookKeyTemplate{
    companion object {
        fun fromProtoBytes(data:ByteString): SaveWorkbookResponse {
            return AppProtos.SaveWorkbookResponseProto.newBuilder().mergeFrom(data).build().toModel()
        }
    }

    override val isError: Boolean
        get() = errorReport!=null

    override fun isLegal(): Boolean {
        return true
    }
}

fun AppProtos.SaveWorkbookResponseProto.toModel(): SaveWorkbookResponse {
    return SaveWorkbookResponse(
        errorReport = if(this.hasErrorReport()) this.errorReport.toModel() else null,
        wbKey = this.wbKey.toModel(),
        path = this.path
    )
}
