package com.emeraldblast.p6.app.action.workbook.delete_worksheet.rm

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.rpc.document.workbook.msg.IdentifyWorksheetMsg

interface DeleteWorksheetRM {
    fun makeRequest(request: IdentifyWorksheetMsg): Rse<Workbook>
}
