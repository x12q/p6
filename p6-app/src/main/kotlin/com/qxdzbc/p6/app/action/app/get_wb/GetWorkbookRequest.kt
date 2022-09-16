package com.qxdzbc.p6.app.action.app.get_wb

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
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
