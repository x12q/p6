package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import com.qxdzbc.p6.app.common.utils.Rs
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.rpc.document.workbook.msg.IdentifyWorksheetMsg

interface DeleteWorksheetAction {
    fun deleteWorksheetRs(request: IdentifyWorksheetMsg): Rs<Unit, ErrorReport>
}
