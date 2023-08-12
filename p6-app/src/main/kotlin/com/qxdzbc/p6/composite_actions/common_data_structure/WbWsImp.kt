package com.qxdzbc.p6.composite_actions.common_data_structure

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.WsWbProto

data class WbWsImp(
    override val wbKey:WorkbookKey,
    override val wsName:String
) : WbWs {
    companion object{
        fun WsWbProto.toModel(): WbWsImp {
            return WbWsImp(
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
