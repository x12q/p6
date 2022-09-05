package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt

interface DeleteWorksheetAction {
    fun deleteWorksheetRs(request: WorksheetIdPrt): Rs<Unit, ErrorReport>
}
