package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.google.protobuf.ByteString
import com.qxdzbc.p6.proto.AppProtos

data class LoadWorkbookRequest(val path:String, override val windowId:String) : RequestToP6WithWindowId {
    fun toProto(): AppProtos.LoadWorkbookRequestProto {
        return AppProtos.LoadWorkbookRequestProto.newBuilder()
            .setPath(this.path)
            .build()
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
}
