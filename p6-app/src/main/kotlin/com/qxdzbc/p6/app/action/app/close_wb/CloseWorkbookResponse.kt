package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport

data class CloseWorkbookResponse(
    override val isError: Boolean,
    override val wbKey: WorkbookKey?,
    override val windowId: String?,
    override val errorReport: ErrorReport?,
): ResponseWithWindowIdAndWorkbookKey {

    override fun isLegal():Boolean{
        if(!isError){
            return errorReport == null
        }
        return true
    }
}
