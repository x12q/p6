package com.emeraldblast.p6.app.action.workbook.new_worksheet

import com.emeraldblast.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.proto.WorkbookProtos
import com.emeraldblast.p6.proto.rpc.workbook.WorkbooKServiceProtos
import com.google.protobuf.ByteString

data class CreateNewWorksheetRequest(
    override val wbKey: WorkbookKey,
    val newWorksheetName: String?
) : RequestToP6WithWorkbookKey {

    companion object
    {
        fun WorkbooKServiceProtos.CreateNewWorksheetRequestProto.toModel(): CreateNewWorksheetRequest {
            return CreateNewWorksheetRequest(
                wbKey= this.wbKey.toModel(),
                newWorksheetName = if(this.hasNewWorksheetName()) this.newWorksheetName else null
            )
        }
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
    fun toProto(): WorkbookProtos.CreateNewWorksheetRequestProto {
        val rt = WorkbookProtos.CreateNewWorksheetRequestProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setNewWorksheetName(newWorksheetName)
            .build()
        return rt
    }
}


