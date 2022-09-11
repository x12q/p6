package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.google.protobuf.ByteString
import com.qxdzbc.p6.proto.AppProtos.LoadWorkbookRequestProto

data class LoadWorkbookRequest(val path:String, override val windowId:String?) : RequestToP6WithWindowId {
    fun toProto(): LoadWorkbookRequestProto {
        return LoadWorkbookRequestProto.newBuilder()
            .setPath(this.path)
            .build()
    }

    companion object{
        fun LoadWorkbookRequestProto.toModel():LoadWorkbookRequest{
            return LoadWorkbookRequest(path, null)
        }
    }
}
