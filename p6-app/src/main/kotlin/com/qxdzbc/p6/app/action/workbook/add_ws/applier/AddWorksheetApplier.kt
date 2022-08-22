package com.qxdzbc.p6.app.action.workbook.add_ws.applier

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.rpc.document.workbook.msg.AddWorksheetResponse

interface AddWorksheetApplier {
    fun applyAddWs(res: RseNav<AddWorksheetResponse>): RseNav<AddWorksheetResponse>
}