package com.qxdzbc.p6.ui.window.workbook_tab.bar

import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.action.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType = WorkbookTabBarAction::class)
class WorkbookTabBarActionImp @Inject constructor(
    private val windowAction: WindowAction,
    private val moveToWb: MoveToWbAction,
    private val openCloseSaveDialogAct: OpenCloseSaveDialogOnWindowAction,
) : WorkbookTabBarAction,
    MoveToWbAction by moveToWb,
    OpenCloseSaveDialogOnWindowAction by openCloseSaveDialogAct {

    override fun createNewWb(windowId: String) {
        windowAction.createNewWorkbook(windowId)
    }

    override fun close(wbKey: WorkbookKey, windowId: String) {
        windowAction.closeWorkbook(wbKey, windowId)
    }
}
