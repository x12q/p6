package com.emeraldblast.p6.ui.window.move_to_wb

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

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
