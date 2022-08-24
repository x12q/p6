package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.rpc.document.workbook.msg.IdentifyWorksheetMsg

interface DeleteWorksheetAction {
    fun deleteWorksheetRs(request: IdentifyWorksheetMsg): Rs<Unit, ErrorReport>
}
