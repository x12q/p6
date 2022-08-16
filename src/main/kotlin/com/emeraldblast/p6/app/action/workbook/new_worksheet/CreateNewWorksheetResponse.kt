package com.emeraldblast.p6.app.action.workbook.new_worksheet

import com.emeraldblast.p6.app.common.proto.toModel
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.WorkbookProtos
import com.google.protobuf.ByteString

data class CreateNewWorksheetResponse(
    override val wbKey: WorkbookKey,
    val newWorksheetName: String,
    override val isError: Boolean,
    override val errorReport: ErrorReport?,
):ResponseWithWorkbookKeyTemplate{
    companion object {
        fun fromProtoBytes(protoBytes:ByteString): CreateNewWorksheetResponse {
            val rt = WorkbookProtos.CreateNewWorksheetResponseProto.newBuilder()
                .mergeFrom(protoBytes)
                .build().toModel()
            return rt
        }
    }

    override fun isLegal(): Boolean {
        return true
    }
}

fun WorkbookProtos.CreateNewWorksheetResponseProto.toModel(): CreateNewWorksheetResponse {
    return CreateNewWorksheetResponse(
        wbKey = workbookKey.toModel(),
        newWorksheetName = newWorksheetName,
        isError = isError,
        errorReport = if (hasErrorReport()) {
            errorReport.toModel()
        } else {
            null
        }
    )
}
