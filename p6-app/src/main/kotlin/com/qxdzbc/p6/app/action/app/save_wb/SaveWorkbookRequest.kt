package com.qxdzbc.p6.app.action.app.save_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.WorkbookProtos.SaveWorkbookRequestProto
import com.google.protobuf.ByteString

data class SaveWorkbookRequest(
    override val wbKey:WorkbookKey,
    val path:String,
): RequestToP6WithWorkbookKey {
    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
    fun toProto():SaveWorkbookRequestProto{
        return SaveWorkbookRequestProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setPath(path)
            .build()
    }
}


