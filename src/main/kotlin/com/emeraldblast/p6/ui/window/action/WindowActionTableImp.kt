package com.emeraldblast.p6.ui.window.action

import com.emeraldblast.p6.ui.script_editor.action.CodeEditorAction
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.ui.document.workbook.action.WorkbookActionTable
import com.emeraldblast.p6.ui.window.file_dialog.action.FileDialogAction
import com.emeraldblast.p6.ui.window.menu.action.CodeMenuAction
import com.emeraldblast.p6.ui.window.menu.action.FileMenuAction
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import javax.inject.Inject

class WindowActionTableImp @Inject constructor(
    override val fileMenuAction:FileMenuAction,
    override val workbookActionTable: WorkbookActionTable,
    override  val wbTabBarAction:WorkbookTabBarAction,
    override  val codeMenuAction: CodeMenuAction,
    override val codeEditorAction:CodeEditorAction,
    override val windowAction: WindowAction,
) : WindowActionTable {
    override val saveFileDialogAction: FileDialogAction
        get() = TODO("Not yet implemented")

}
