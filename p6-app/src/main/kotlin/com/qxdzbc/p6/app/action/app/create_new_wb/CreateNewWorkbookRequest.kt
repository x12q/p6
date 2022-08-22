package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.qxdzbc.p6.proto.AppEventProtos.CreateNewWorkbookRequestProto
import com.google.protobuf.ByteString

data class CreateNewWorkbookRequest(
    override val windowId:String?,
): RequestToP6WithWindowId {
    fun toProto():CreateNewWorkbookRequestProto{
        val bd=CreateNewWorkbookRequestProto.newBuilder()
        if(this.windowId!=null){
            bd.setWindowId(this.windowId)
        }
        return bd.build()
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
}
