package com.qxdzbc.p6.app.action.workbook.set_active_ws.rm

import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetWithIndexRequest
import com.qxdzbc.p6.app.common.utils.RseNav


interface SetActiveWorksheetRM {
    fun setActiveWs(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2>
    fun setActiveWsWithIndex(request: SetActiveWorksheetWithIndexRequest): RseNav<SetActiveWorksheetResponse2>
}

