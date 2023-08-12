package com.qxdzbc.p6.composite_actions.window

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.composite_actions.app.process_save_path.MakeSavePath
import com.qxdzbc.p6.composite_actions.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.composite_actions.app.set_active_wd.SetActiveWindowAction
import com.qxdzbc.p6.composite_actions.window.close_window.CloseWindowAction
import com.qxdzbc.p6.composite_actions.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.ui.window.tool_bar.action.ToolBarAction
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import java.nio.file.Path

interface WindowAction : SaveWorkbookAction, SetActiveWindowAction,CloseWindowAction, MakeSavePath,
    OpenCloseSaveDialogOnWindowAction {
    val toolBarAction: ToolBarAction
    fun showCommonFileDialog(job: CompletableDeferred<Path?>, windowId: String)
    fun openCommonFileBrowserAndUpdatePath(tMs: Ms<String>, executionScope: CoroutineScope, windowId: String)
    fun closeCommonFileDialog(windowId: String)
    fun setCommonFileDialogResult(path: Path?, windowId: String)
    fun onFatalError()
    fun saveActiveWorkbook(path: Path?, windowId: String)
    fun openLoadFileDialog(windowId: String)
    fun loadWorkbook(path: Path?, windowId: String)
    fun closeLoadFileDialog(windowId: String)
    fun createNewWorkbook(windowId: String)
    fun closeWorkbook(workbookKey: WorkbookKey, windowId: String)
}
