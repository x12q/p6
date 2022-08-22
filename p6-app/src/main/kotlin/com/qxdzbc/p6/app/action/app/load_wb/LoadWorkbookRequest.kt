package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.qxdzbc.p6.proto.AppEventProtos.LoadWorkbookRequestProto
import com.google.protobuf.ByteString

data class LoadWorkbookRequest(val path:String, override val windowId:String) : RequestToP6WithWindowId {
    fun toProto():LoadWorkbookRequestProto{
        return LoadWorkbookRequestProto.newBuilder()
            .setPath(this.path)
            .setWindowId(this.windowId)
            .build()
    }

    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
}
