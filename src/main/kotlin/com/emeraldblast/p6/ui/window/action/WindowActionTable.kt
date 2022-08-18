package com.emeraldblast.p6.ui.window.action

import com.emeraldblast.p6.ui.script_editor.action.CodeEditorAction
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.ui.document.workbook.action.WorkbookActionTable
import com.emeraldblast.p6.ui.window.file_dialog.action.FileDialogAction
import com.emeraldblast.p6.ui.window.menu.action.CodeMenuAction
import com.emeraldblast.p6.ui.window.menu.action.FileMenuAction
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction

/**
 * provide action objs for window view and its children view
 */
interface WindowActionTable{
    val windowAction: WindowAction
    val fileMenuAction: FileMenuAction

    val wbTabBarAction: WorkbookTabBarAction
    val saveFileDialogAction: FileDialogAction
    val workbookActionTable: WorkbookActionTable

    val codeEditorAction: CodeEditorAction
    val codeMenuAction: CodeMenuAction
}
