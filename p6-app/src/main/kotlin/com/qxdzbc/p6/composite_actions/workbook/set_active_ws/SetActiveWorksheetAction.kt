package com.qxdzbc.p6.composite_actions.workbook.set_active_ws

import com.qxdzbc.p6.common.utils.RseNav


interface SetActiveWorksheetAction {
    fun setActiveWs(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2>
    fun setActiveWsUsingIndex(request: SetActiveWorksheetWithIndexRequest): RseNav<SetActiveWorksheetResponse2>
}
