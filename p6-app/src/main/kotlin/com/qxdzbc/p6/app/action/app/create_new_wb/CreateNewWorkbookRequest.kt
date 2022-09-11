package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.google.protobuf.ByteString
import com.qxdzbc.p6.proto.AppProtos

data class CreateNewWorkbookRequest(
    override val windowId:String?,
): RequestToP6WithWindowId {
    fun toProto(): AppProtos.CreateNewWorkbookRequestProto {
        val bd= AppProtos.CreateNewWorkbookRequestProto.newBuilder()
        if(this.windowId!=null){
            bd.setWindowId(this.windowId)
        }
        return bd.build()
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
}
