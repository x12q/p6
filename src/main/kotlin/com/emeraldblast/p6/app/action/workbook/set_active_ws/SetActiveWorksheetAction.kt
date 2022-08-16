package com.emeraldblast.p6.app.action.workbook.set_active_ws

import com.emeraldblast.p6.app.common.RseNav


interface SetActiveWorksheetAction {
    fun setActiveWs(request: SetActiveWorksheetRequest):RseNav<SetActiveWorksheetResponse2>
    fun setActiveWsUsingIndex(request: SetActiveWorksheetWithIndexRequest):RseNav<SetActiveWorksheetResponse2>
}
