package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.WorksheetProtos.WorksheetIdProto

data class WorksheetIdPrt(
    val wbKey:WorkbookKey,
    val wsName:String,
) {
    companion object {
        fun WorksheetIdProto.toModel(): WorksheetIdPrt {
            return WorksheetIdPrt(
                wbKey = this.wbKey.toModel(),
                wsName =  this.wsName,
            )
        }
    }

    fun toProto():WorksheetIdProto{
        return WorksheetIdProto.newBuilder()
            .setWbKey(this.wbKey.toProto())
            .setWsName(this.wsName)
            .build()
    }
}

