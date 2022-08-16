package com.emeraldblast.p6.app.action.workbook.set_active_ws

import com.emeraldblast.p6.ui.app.ActiveWindowPointer
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.window.state.ActiveWorkbookPointer
import com.emeraldblast.p6.ui.window.state.WindowState

class SetActiveWorksheetResponse2(
    val request: SetActiveWorksheetRequest,
    val newWbState:WorkbookState?,
    val newActiveWindowPointer:ActiveWindowPointer?,
    val newActiveWbPointer:ActiveWorkbookPointer?,
)
