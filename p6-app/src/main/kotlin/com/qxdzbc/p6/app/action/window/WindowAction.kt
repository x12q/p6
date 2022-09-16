package com.qxdzbc.p6.app.action.window

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.path.PPath
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowAction
import com.qxdzbc.p6.ui.kernel.KernelAction
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import java.nio.file.Path

interface WindowAction : KernelAction,SaveWorkbookAction, SetActiveWindowAction {
    fun showCommonFileDialog(job: CompletableDeferred<Path?>, windowId: String)
    fun openCommonFileBrowserAndUpdatePath(tMs: Ms<String>, executionScope: CoroutineScope, windowId: String)
    fun closeCommonFileDialog(windowId: String)
    fun setCommonFileDialogResult(path: Path?, windowId: String)
    fun openDialogToStartKernel(windowId: String)
    fun closeDialogToStartKernel(windowId: String)
    fun openDialogToConnectToKernel(windowId: String)
    fun closeDialogToConnectToKernel(windowId: String)
    fun onCloseWindowRequest(windowId: String)
    fun onFatalError()
    fun saveActiveWorkbook(path: Path?, windowId: String)
    fun openSaveFileDialog(windowId: String)
    fun closeSaveFileDialog(windowId: String)
    fun openLoadFileDialog(windowId: String)
    fun loadWorkbook(path: Path?, windowId: String)
    fun closeLoadFileDialog(windowId: String)
    fun createNewWorkbook(windowId: String)
    fun closeWorkbook(workbookKey: WorkbookKey,windowId: String)
}


