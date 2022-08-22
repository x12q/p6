package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKeyAndWindowId
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.AppEventProtos.CloseWorkbookRequestProto
import com.google.protobuf.ByteString

class CloseWorkbookRequest(
    override val windowId:String?,
    override val wbKey: WorkbookKey
) : RequestToP6WithWorkbookKeyAndWindowId {
    override fun toProtoBytes():ByteString{
        val b = CloseWorkbookRequestProto.newBuilder()
            .setWorkbookKey(this.wbKey.toProto())
        if (this.windowId!=null){
            b.setWindowId(this.windowId)
        }
        return b.build().toByteString()
    }
}
