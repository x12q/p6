package com.qxdzbc.p6.app.action.app.save_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.google.protobuf.ByteString
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.AppProtos

data class SaveWorkbookRequest(
    override val wbKey:WorkbookKey,
    val path:String,
): RequestToP6WithWorkbookKey {
    companion object{
        fun AppProtos.SaveWorkbookRequestProto.toModel():SaveWorkbookRequest{
            return SaveWorkbookRequest(
                wbKey = wbKey.toModel(),
                path = path
            )
        }
    }

//    override fun toProtoBytes(): ByteString {
//        return this.toProto().toByteString()
//    }
    fun toProto(): AppProtos.SaveWorkbookRequestProto {
        return AppProtos.SaveWorkbookRequestProto.newBuilder()
            .setWbKey(wbKey.toProto())
            .setPath(path)
            .build()
    }
}


