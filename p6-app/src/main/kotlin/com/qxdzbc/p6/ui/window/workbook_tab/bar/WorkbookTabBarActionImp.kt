package com.qxdzbc.p6.ui.window.workbook_tab.bar

import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.action.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.action.move_focus_to_wb.MoveFocusToWbAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class, boundType = WorkbookTabBarAction::class)
class WorkbookTabBarActionImp @Inject constructor(
    private val windowAction: WindowAction,
    private val moveToWb: MoveFocusToWbAction,
    private val openCloseSaveDialogAct: OpenCloseSaveDialogOnWindowAction,
) : WorkbookTabBarAction,
    MoveFocusToWbAction by moveToWb,
    OpenCloseSaveDialogOnWindowAction by openCloseSaveDialogAct {

    override fun createNewWb(windowId: String) {
        windowAction.createNewWorkbook(windowId)
    }

    override fun close(wbKey: WorkbookKey, windowId: String) {
        windowAction.closeWorkbook(wbKey, windowId)
    }
}
