package com.qxdzbc.p6.ui.window.workbook_tab.bar

import com.qxdzbc.p6.app.action.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.window.action.move_focus_to_wb.MoveFocusToWbAction

interface WorkbookTabBarAction : MoveFocusToWbAction, OpenCloseSaveDialogOnWindowAction {
    fun createNewWb(windowId: String)
    fun close(wbKey: WorkbookKey,windowId: String)
}

