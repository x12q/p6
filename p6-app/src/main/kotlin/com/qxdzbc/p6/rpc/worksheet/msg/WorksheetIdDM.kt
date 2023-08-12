package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.WorksheetIdProto

data class WorksheetIdDM(
    override val wbKey:WorkbookKey,
    override val wsName:String,
) :WbWs{
    companion object {
        fun WorksheetIdProto.toModelDM(): WorksheetIdDM {
            return WorksheetIdDM(
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

