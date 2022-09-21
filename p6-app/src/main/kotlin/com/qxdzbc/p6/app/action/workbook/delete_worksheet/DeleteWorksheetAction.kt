package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt

interface DeleteWorksheetAction {
    fun deleteWorksheetRs(request: WorksheetIdWithIndexPrt): Rs<Unit, ErrorReport>
}
