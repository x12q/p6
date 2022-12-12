package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.qxdzbc.p6.proto.AppProtos.CreateNewWorkbookRequestProto

data class CreateNewWorkbookRequest(
    override val windowId:String?,
    val wbName:String? = null,
): RequestToP6WithWindowId {
    companion object{
        fun CreateNewWorkbookRequestProto.toModel():CreateNewWorkbookRequest{
            return CreateNewWorkbookRequest(
                windowId = if(this.hasWindowId()) this.windowId else null,
                wbName = if (this.hasWorkbookName()) this.workbookName else null,
            )
        }
    }
    fun toProto(): CreateNewWorkbookRequestProto {
        val bd= CreateNewWorkbookRequestProto
            .newBuilder()
        if(this.windowId!=null){
            bd.setWindowId(this.windowId)
        }
        if(this.wbName!=null){
            bd.setWorkbookName(this.wbName)
        }
        return bd.build()
    }

//    override fun toProtoBytes(): ByteString {
//        return this.toProto().toByteString()
//    }
}
