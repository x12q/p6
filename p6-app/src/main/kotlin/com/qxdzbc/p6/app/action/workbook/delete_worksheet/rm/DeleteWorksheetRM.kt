package com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdWithIndexPrt

interface DeleteWorksheetRM {
    fun makeRequest(request: WorksheetIdWithIndexPrt): Rse<Workbook>
}
