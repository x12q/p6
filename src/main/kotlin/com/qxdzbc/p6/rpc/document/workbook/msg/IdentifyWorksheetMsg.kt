package com.qxdzbc.p6.rpc.document.workbook.msg

import com.qxdzbc.p6.app.communication.res_req_template.WithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.rpc.workbook.WorkbooKServiceProtos.IdentifyWorksheetMsgProto

data class IdentifyWorksheetMsg(
    val wbKey: WorkbookKey,
    val wsName: String?,
    val wsIndex: Int?
) {
    companion object {
        fun IdentifyWorksheetMsgProto.toModel(): IdentifyWorksheetMsg {
            return IdentifyWorksheetMsg(
                wbKey = this.wbKey.toModel(),
                wsName = if (this.hasWsName()) this.wsName else null,
                wsIndex = if (this.hasWsIndex()) this.wsIndex else null,
            )
        }
    }
    fun isLegal():Boolean{
        return wsName!=null || wsIndex!=null
    }
}
