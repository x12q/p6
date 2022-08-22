package com.qxdzbc.p6.app.action.worksheet.rename_ws

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.proto.WorksheetProtos
import com.google.protobuf.ByteString

data class RenameWorksheetResponse(
    override val wbKey: WorkbookKey,
    val oldName:String,
    val newName:String,
    override val isError:Boolean = false,
    override val errorReport:ErrorReport? = null
) :ResponseWithWorkbookKeyTemplate{

    companion object {
        fun fromProtoBytes(protoBytes:ByteString): RenameWorksheetResponse {
            return WorksheetProtos.RenameWorksheetResponseProto.newBuilder().mergeFrom(protoBytes).build().toModel()
        }
    }
    /**
     * for testing, consider move it to test package
     */
    fun toProto(): WorksheetProtos.RenameWorksheetResponseProto{
        val builder = WorksheetProtos.RenameWorksheetResponseProto.newBuilder()
            .setWorkbookKey(this.wbKey.toProto())
            .setOldName(oldName)
            .setNewName(newName)
            .setIsError(isError)
        if (this.errorReport!=null){
            builder.setErrorReport(this.errorReport.toProto())
        }
        val rt= builder.build()
        return rt
    }

    override fun isLegal(): Boolean {
        return true
    }
}

fun WorksheetProtos.RenameWorksheetResponseProto.toModel(): RenameWorksheetResponse {
    val rt = RenameWorksheetResponse(
        wbKey = workbookKey.toModel(),
        oldName = oldName,
        newName = newName,
        isError = isError,
        errorReport = if (this.hasErrorReport()) {
            this.errorReport.toModel()
        } else {
            null
        }
    )
    return rt
}

