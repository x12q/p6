package com.qxdzbc.p6.composite_actions.workbook.set_active_ws

import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.state.ActiveWorkbookPointer

class SetActiveWorksheetResponse2(
    val request: SetActiveWorksheetRequest,
    val newWbState:WorkbookState?,
    val newActiveWindowPointer:ActiveWindowPointer?,
    val newActiveWbPointer:ActiveWorkbookPointer?,
)
