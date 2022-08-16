package com.emeraldblast.p6.ui.action_table

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.script_editor.action.CodeEditorAction
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.ui.window.file_dialog.action.FileDialogAction
import com.emeraldblast.p6.ui.window.menu.action.CodeMenuAction
import com.emeraldblast.p6.ui.window.menu.action.FileMenuAction
import com.emeraldblast.p6.ui.window.state.WindowState
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import javax.inject.Inject

class WindowActionTableImp @Inject constructor(
    private val fileMenuAction:FileMenuAction,
    override val workbookActionTable: WorkbookActionTable,
    private val wbTabBarAction:WorkbookTabBarAction,
    private  val codeMenuAction: CodeMenuAction,
    private val codeEditorAction:CodeEditorAction,
    private val windowAction: WindowAction,
) : WindowActionTable {

    override fun getWindowAction(): WindowAction {
        return windowAction
    }

    override fun getFileMenuAction(): FileMenuAction {
        return fileMenuAction
    }

    override fun getCodeMenuAction(): CodeMenuAction {
        return codeMenuAction
    }

    override fun getWbTabBarAction(): WorkbookTabBarAction {
        return wbTabBarAction
    }

    override fun getSaveFileDialogAction(): FileDialogAction {
        TODO("Not yet implemented")
    }

    override fun getCodeEditorAction(): CodeEditorAction {
        return codeEditorAction
    }
}
