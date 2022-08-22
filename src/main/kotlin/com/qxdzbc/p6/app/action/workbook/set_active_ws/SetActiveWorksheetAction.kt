package com.qxdzbc.p6.app.action.workbook.set_active_ws

import com.qxdzbc.p6.app.common.utils.RseNav


interface SetActiveWorksheetAction {
    fun setActiveWs(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2>
    fun setActiveWsUsingIndex(request: SetActiveWorksheetWithIndexRequest): RseNav<SetActiveWorksheetResponse2>
}
