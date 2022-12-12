package com.qxdzbc.p6.app.action.app.save_wb

import com.google.protobuf.ByteString
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWith_WbKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.AppProtos.SaveWorkbookResponseProto

class SaveWorkbookResponse(
    override val errorReport: ErrorReport?,
    override val wbKey: WorkbookKey,
    val path: String,
) : ResponseWith_WbKey {
    companion object {
        fun fromProtoBytes(data: ByteString): SaveWorkbookResponse {
            return SaveWorkbookResponseProto.newBuilder().mergeFrom(data).build().toModel()
        }
    }

    override val isError: Boolean
        get() = errorReport != null


    fun toProto(): SaveWorkbookResponseProto {
        return SaveWorkbookResponseProto.newBuilder()
            .apply {
                val e = this@SaveWorkbookResponse.errorReport
                if (e != null) {
                    setErrorReport(e.toProto())
                }
            }
            .setWbKey(wbKey.toProto())
            .setPath(path)
            .build()
    }
}

fun SaveWorkbookResponseProto.toModel(): SaveWorkbookResponse {
    return SaveWorkbookResponse(
        errorReport = if (this.hasErrorReport()) this.errorReport.toModel() else null,
        wbKey = this.wbKey.toModel(),
        path = this.path
    )
}
