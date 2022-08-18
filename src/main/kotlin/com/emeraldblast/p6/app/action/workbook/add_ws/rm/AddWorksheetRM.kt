package com.emeraldblast.p6.app.action.workbook.add_ws.rm

import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetRequest
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetResponse

interface AddWorksheetRM {
    fun makeAddWsRequest(req:AddWorksheetRequest): RseNav<AddWorksheetResponse>
}
