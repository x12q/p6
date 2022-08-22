package com.qxdzbc.p6.app.action.worksheet.rename_ws

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.proto.rpc.workbook.WorkbooKServiceProtos
import com.google.protobuf.ByteString


/**
 * Data structure for renaming worksheet request. Tied to a proto definition
 */
data class RenameWorksheetRequest(
    override val wbKey: WorkbookKey,
    val oldName: String,
    val newName: String
) : RequestToP6WithWorkbookKey {

    companion object {
        fun WorkbooKServiceProtos.RenameWorksheetRequestProto.toModel(): RenameWorksheetRequest {
            return RenameWorksheetRequest(
                wbKey = this.wbKey.toModel(),
                oldName = this.oldName,
                newName = this.newName
            )
        }
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }

    fun toProto(): WorksheetProtos.RenameWorksheetRequestProto {
        val rt = WorksheetProtos.RenameWorksheetRequestProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setOldName(oldName)
            .setNewName(newName)
            .build()
        return rt
    }
}
