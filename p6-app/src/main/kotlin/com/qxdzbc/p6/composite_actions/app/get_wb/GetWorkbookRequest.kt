package com.qxdzbc.p6.composite_actions.app.get_wb

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.AppProtos.GetWorkbookRequestProto

data class GetWorkbookRequest(
    val wbKey: WorkbookKey? = null,
    val wbName: String? = null,
    val wbIndex: Int? = null,
) {
    companion object {
        fun GetWorkbookRequestProto.toModel():GetWorkbookRequest{
            val p = this
            return GetWorkbookRequest(
                wbKey = if (p.hasWbKey()) p.wbKey.toModel() else null,
                wbName = if (p.hasWbName()) p.wbName else null,
                wbIndex = if (p.hasWbIndex()) p.wbIndex else null,
            )
        }
    }
}
