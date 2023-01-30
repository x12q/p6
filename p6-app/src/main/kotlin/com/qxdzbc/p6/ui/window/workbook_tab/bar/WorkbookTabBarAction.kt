package com.qxdzbc.p6.ui.window.workbook_tab.bar

import com.qxdzbc.p6.app.action.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbAction

interface WorkbookTabBarAction : MoveToWbAction, OpenCloseSaveDialogOnWindowAction {
    fun createNewWb(windowId: String)
    fun close(wbKey: WorkbookKey,windowId: String)
}

