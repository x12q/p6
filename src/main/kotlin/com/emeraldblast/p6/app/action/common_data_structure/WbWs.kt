package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.proto.DocProtos.WsWbProto

data class WbWs(
    override val wbKey:WorkbookKey,
    override val wsName:String
) : WithWbWs {
    companion object{
        fun WsWbProto.toModel(): WbWs {
            return WbWs(
                wbKey = this.workbookKey.toModel(),
                wsName = this.worksheetName
            )
        }
    }

    fun toProto():WsWbProto{
        return WsWbProto.newBuilder()
            .setWorkbookKey(this.wbKey.toProto())
            .setWorksheetName(this.wsName)
            .build()
    }
}
