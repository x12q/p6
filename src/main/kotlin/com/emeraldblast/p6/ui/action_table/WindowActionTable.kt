package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.script_editor.action.CodeEditorAction
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.ui.window.file_dialog.action.FileDialogAction
import com.emeraldblast.p6.ui.window.menu.action.CodeMenuAction
import com.emeraldblast.p6.ui.window.menu.action.FileMenuAction
import com.emeraldblast.p6.ui.window.state.WindowState
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction

/**
 * provide action objs for window view and its children view
 */
interface WindowActionTable{
    fun getWindowAction(): WindowAction
    fun getFileMenuAction(): FileMenuAction

    fun getWbTabBarAction(): WorkbookTabBarAction
    fun getSaveFileDialogAction(): FileDialogAction
    val workbookActionTable:WorkbookActionTable

    fun getCodeEditorAction(): CodeEditorAction
    fun getCodeMenuAction(): CodeMenuAction
}
