package com.emeraldblast.p6.app.action.workbook.click_on_ws_tab_item

import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.emeraldblast.p6.app.common.utils.RseNav

interface SwitchWorksheetAction {
    fun switchToWorksheet(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2>
}
