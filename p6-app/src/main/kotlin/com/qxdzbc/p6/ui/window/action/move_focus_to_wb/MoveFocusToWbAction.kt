package com.qxdzbc.p6.ui.window.action.move_focus_to_wb

import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface MoveFocusToWbAction {
    /**
     * Comprehensive code to move user's focus to another workbook, perform all the necessary side effect
     */
    fun moveToWorkbook(wbKey: WorkbookKey)

    /**
     * only set active workbook
     */
    fun setActiveWb(wbKey: WorkbookKey)
}
