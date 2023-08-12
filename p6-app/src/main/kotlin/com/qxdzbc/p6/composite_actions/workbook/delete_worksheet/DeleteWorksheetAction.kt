package com.qxdzbc.p6.composite_actions.workbook.delete_worksheet

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt

interface DeleteWorksheetAction {
    fun deleteWorksheetRs(request: WorksheetIdWithIndexPrt): Rse<Unit>
}
