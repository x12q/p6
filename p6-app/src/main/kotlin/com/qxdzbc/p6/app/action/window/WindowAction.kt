package com.qxdzbc.p6.app.action.window

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.process_save_path.MakeSavePath
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowAction
import com.qxdzbc.p6.app.action.window.close_window.CloseWindowAction
import com.qxdzbc.p6.app.action.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.window.dialog.WindowDialogGroupAction
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
    fun openDialogToStartKernel(windowId: String)
    fun closeDialogToStartKernel(windowId: String)
    fun openDialogToConnectToKernel(windowId: String)
    fun closeDialogToConnectToKernel(windowId: String)
    fun onFatalError()
    fun saveActiveWorkbook(path: Path?, windowId: String)
    fun openLoadFileDialog(windowId: String)
    fun loadWorkbook(path: Path?, windowId: String)
    fun closeLoadFileDialog(windowId: String)
    fun createNewWorkbook(windowId: String)
    fun closeWorkbook(workbookKey: WorkbookKey, windowId: String)

    val windowDialogGroupAction: WindowDialogGroupAction
}
