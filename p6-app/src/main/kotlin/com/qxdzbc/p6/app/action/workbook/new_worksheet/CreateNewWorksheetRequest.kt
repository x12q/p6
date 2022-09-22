package com.qxdzbc.p6.app.action.workbook.new_worksheet

import com.google.protobuf.ByteString
import com.qxdzbc.p6.app.action.WbWsRequest
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.WorkbookProtos

data class CreateNewWorksheetRequest(
    override val wbKey: WorkbookKey,
    val newWorksheetName: String?
) : RequestToP6WithWorkbookKey,WbWs {

    companion object
    {
        fun WorkbookProtos.CreateNewWorksheetRequestProto.toModel(): CreateNewWorksheetRequest {
            return CreateNewWorksheetRequest(
                wbKey= this.wbKey.toModel(),
                newWorksheetName = if(this.hasNewWorksheetName()) this.newWorksheetName else null
            )
        }
    }

//    override fun toProtoBytes(): ByteString {
//        return this.toProto().toByteString()
//    }
    fun toProto(): WorkbookProtos.CreateNewWorksheetRequestProto {
        val rt = WorkbookProtos.CreateNewWorksheetRequestProto.newBuilder()
            .setWbKey(wbKey.toProto())
            .apply{
                this@CreateNewWorksheetRequest.newWorksheetName?.also {
                    setNewWorksheetName(it)
                }
            }
            .build()
        return rt
    }

    override val wsName: String
        get() = this.newWorksheetName ?: ""
}


