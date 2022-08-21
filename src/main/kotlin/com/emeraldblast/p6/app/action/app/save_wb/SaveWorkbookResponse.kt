package com.emeraldblast.p6.app.action.app.save_wb

import com.emeraldblast.p6.app.common.proto.ProtoUtils.toModel
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.WorkbookProtos.SaveWorkbookResponseProto
import com.google.protobuf.ByteString

class SaveWorkbookResponse(
    override val isError: Boolean,
    override val errorReport: ErrorReport?,
    override val wbKey:WorkbookKey,
    val path:String,
) :ResponseWithWorkbookKeyTemplate{
    companion object {
        fun fromProtoBytes(data:ByteString): SaveWorkbookResponse {
            return SaveWorkbookResponseProto.newBuilder().mergeFrom(data).build().toModel()
        }
    }

    override fun isLegal(): Boolean {
        return true
    }
}

fun SaveWorkbookResponseProto.toModel(): SaveWorkbookResponse {
    return SaveWorkbookResponse(
        isError = this.isError,
        errorReport = if(this.hasErrorReport()) this.errorReport.toModel() else null,
        wbKey = this.workbookKey.toModel(),
        path = this.path
    )
}
