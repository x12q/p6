package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.WorksheetProtos

data class WorksheetIdWithIndexPrt(
    val wbKey: WorkbookKey,
    val wsName:String?,
    val wsIndex:Int?
) {
    companion object {
        fun WorksheetProtos.WorksheetIdWithIndexProto.toModel(): WorksheetIdWithIndexPrt {
            return WorksheetIdWithIndexPrt(
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
