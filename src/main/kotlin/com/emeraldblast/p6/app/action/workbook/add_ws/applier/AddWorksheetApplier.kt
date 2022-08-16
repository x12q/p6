package com.emeraldblast.p6.app.action.workbook.add_ws.applier

import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetResponse

interface AddWorksheetApplier {
    fun applyAddWs(res:RseNav<AddWorksheetResponse>):RseNav<AddWorksheetResponse>
}
