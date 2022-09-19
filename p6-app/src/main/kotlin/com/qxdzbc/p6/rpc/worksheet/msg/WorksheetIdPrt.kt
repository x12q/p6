package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto

data class WorksheetIdPrt(
    override val wbKey:WorkbookKey,
    override val wsName:String,
) :WbWs{
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

