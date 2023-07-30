package com.qxdzbc.p6.ui.window.action

import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable
import com.qxdzbc.p6.ui.window.file_dialog.action.FileDialogAction
import com.qxdzbc.p6.ui.window.menu.action.FileMenuAction
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class WindowActionTableImp @Inject constructor(
    override val fileMenuAction:FileMenuAction,
    override val workbookActionTable: WorkbookActionTable,
    override  val wbTabBarAction:WorkbookTabBarAction,
    override val windowAction: WindowAction,
) : WindowActionTable {
    override val saveFileDialogAction: FileDialogAction
        get() = TODO("Not yet implemented")
}
