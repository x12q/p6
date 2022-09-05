package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.WorksheetProtos.WorksheetIdProto

data class WorksheetIdPrt(
    val wbKey:WorkbookKey,
    val wsName:String?,
    val wsIndex:Int?
) {
    companion object {
        fun WorksheetIdProto.toModel(): WorksheetIdPrt {
            return WorksheetIdPrt(
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
