package com.qxdzbc.p6.ui.window.move_to_wb

import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface MoveToWbAction {
    /**
     * Comprehensive code to move to another workbook, perform all the necessary side effect
     */
    fun moveToWorkbook(wbKey: WorkbookKey)

    /**
     * only set active workbook
     */
    fun setActiveWb(wbKey: WorkbookKey)
}
