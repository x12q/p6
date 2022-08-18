package com.emeraldblast.p6.app.action.workbook.add_ws

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetRequest
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetResponse

interface AddWorksheetAction {
    fun addWorksheetRs(request: AddWorksheetRequest): Rse<AddWorksheetResponse>
}
