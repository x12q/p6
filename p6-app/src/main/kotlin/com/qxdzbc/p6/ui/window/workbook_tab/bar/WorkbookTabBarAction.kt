package com.qxdzbc.p6.ui.window.workbook_tab.bar

import com.qxdzbc.p6.composite_actions.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.ui.window.action.move_focus_to_wb.MoveFocusToWbAction

interface WorkbookTabBarAction : MoveFocusToWbAction, OpenCloseSaveDialogOnWindowAction {
    fun createNewWb(windowId: String)
    fun close(wbKey: WorkbookKey,windowId: String)
}

