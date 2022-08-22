package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.proto.WorkbookProtos
import com.google.protobuf.ByteString

data class DeleteWorksheetResponse2(
    val rs: Rse<Workbook>,
    val deletedWsName:String?,
)

data class DeleteWorksheetResponse(
    override val wbKey: WorkbookKey,
    val targetWorksheetName: String,
    override val errorReport: ErrorReport?,
): ResponseWithWorkbookKeyTemplate {
    override val isError: Boolean get()=errorReport!=null
    companion object {
        fun fromProtoBytes(protoBytes:ByteString): DeleteWorksheetResponse {
            val proto = WorkbookProtos.DeleteWorksheetResponseProto.newBuilder().mergeFrom(protoBytes).build()
            return proto.toModel()
        }
    }
    fun toProto(): WorkbookProtos.DeleteWorksheetResponseProto {
        val builder = WorkbookProtos.DeleteWorksheetResponseProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setTargetWorksheet(targetWorksheetName)
            .setIsError(this.isError)
        if (this.errorReport != null) {
            builder.setErrorReport(errorReport.toProto())
        }
        return builder.build()
    }

    override fun isLegal(): Boolean {
        return true
    }

}


fun WorkbookProtos.DeleteWorksheetResponseProto.toModel(): DeleteWorksheetResponse {
    return DeleteWorksheetResponse(
        wbKey = workbookKey.toModel(),
        targetWorksheetName = targetWorksheet,
        errorReport = if (hasErrorReport()) {
            errorReport.toModel()
        } else {
            null
        }
    )
}
