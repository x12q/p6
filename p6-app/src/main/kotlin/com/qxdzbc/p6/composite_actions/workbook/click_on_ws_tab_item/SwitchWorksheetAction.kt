package com.qxdzbc.p6.composite_actions.workbook.click_on_ws_tab_item

import com.qxdzbc.p6.composite_actions.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.composite_actions.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.common.utils.RseNav

interface SwitchWorksheetAction {
    fun switchToWorksheet(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2>
}
