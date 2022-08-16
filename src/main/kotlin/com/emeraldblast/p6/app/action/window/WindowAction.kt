package com.emeraldblast.p6.app.action.window

import com.emeraldblast.p6.app.common.utils.PPath
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.kernel.KernelAction
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.nio.file.Path

interface WindowAction : KernelAction{

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
    fun loadWorkbook(path: PPath?, windowId: String)
    fun closeLoadFileDialog(windowId: String)
    fun createNewWorkbook(windowId: String)
    fun closeWorkbook(workbookKey: WorkbookKey, windowId: String)
    fun saveWorkbook(wbKey: WorkbookKey, path: Path, windowId: String)
}

interface WindowActionInView : KernelAction{
    fun openCommonFileBrowserAndUpdatePath(tMs: Ms<String>, executionScope: CoroutineScope)
    fun showCommonFileDialog(job:CompletableDeferred<Path?>)
    fun closeCommonFileDialog()
    fun setCommonFileDialogResult(path:Path?)

    fun openDialogToStartKernel()
    fun closeDialogToStartKernel()
    fun openDialogToConnectToKernel()
    fun closeDialogToConnectToKernel()

    fun onCloseWindowRequest()
    fun onFatalError()

    fun saveActiveWorkbook(path:Path?)
    fun saveWorkbook(wbKey:WorkbookKey, path: Path)
    fun openSaveFileDialog()
    fun closeSaveFileDialog()

    fun loadWorkbook(path: PPath?)
    fun openLoadFileDialog()
    fun closeLoadFileDialog()

    fun createNewWorkbook()

    fun closeWorkbook(workbookKey: WorkbookKey)

}
