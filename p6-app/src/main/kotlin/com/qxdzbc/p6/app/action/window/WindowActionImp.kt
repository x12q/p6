package com.qxdzbc.p6.app.action.window


import androidx.compose.ui.window.ApplicationScope
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest
import com.qxdzbc.p6.app.action.app.process_save_path.MakeSavePath
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowAction
import com.qxdzbc.p6.app.action.window.close_window.CloseWindowAction
import com.qxdzbc.p6.app.action.window.open_close_save_dialog.OpenCloseSaveDialogOnWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.tool_bar.action.ToolBarAction
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.nio.file.Path
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType = WindowAction::class)
class WindowActionImp @Inject constructor(
    private val appScope: ApplicationScope?,
    private val closeWbAction: CloseWorkbookAction,
    private val subAppStateContainer: StateContainer,
    private val newWbAct: CreateNewWorkbookAction,
    private val saveWbAction: SaveWorkbookAction,
    private val loadWbAction: LoadWorkbookAction,
    private val setActiveWdAct: SetActiveWindowAction,
    private val closeWindowAct: CloseWindowAction,
    private val makeSavePath: MakeSavePath,
    override val toolBarAction: ToolBarAction,
    val openCloseSaveDialog: OpenCloseSaveDialogOnWindowAction,
) : WindowAction,
    MakeSavePath by makeSavePath,
    CloseWindowAction by closeWindowAct,
    SetActiveWindowAction by setActiveWdAct,
    SaveWorkbookAction by saveWbAction,
    OpenCloseSaveDialogOnWindowAction by openCloseSaveDialog {

    override fun openCommonFileBrowserAndUpdatePath(tMs: Ms<String>, executionScope: CoroutineScope, windowId: String) {
        executionScope.launch {
            val d = CompletableDeferred<Path?>()
            showCommonFileDialog(d, windowId)
            val path = d.await()
            if (path != null) {
                tMs.value = path.toAbsolutePath().toString()
            }
        }
    }

    override fun showCommonFileDialog(job: CompletableDeferred<Path?>, windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            it.value = it.value.setCommonFileDialogJob(job)
        }

    }

    override fun closeCommonFileDialog(windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            it.value = it.value.removeCommonFileDialogJob()
        }

    }

    override fun setCommonFileDialogResult(path: Path?, windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            it.value.commonFileDialogJob?.complete(path)
        }
    }

    override fun openDialogToStartKernel(windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            it.value.showStartKernelDialogState = it.value.showStartKernelDialogState.show()
        }
    }

    override fun closeDialogToStartKernel(windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            it.value.showStartKernelDialogState = it.value.showStartKernelDialogState.hide()
        }
    }

    override fun openDialogToConnectToKernel(windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            it.value.showConnectToKernelDialogState = it.value.showConnectToKernelDialogState.show()
        }
    }

    override fun closeDialogToConnectToKernel(windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            it.value.showConnectToKernelDialogState = it.value.showConnectToKernelDialogState.hide()
        }
    }

    override fun onFatalError() {
        appScope?.exitApplication()
    }

    override fun saveActiveWorkbook(path: Path?, windowId: String) {
        subAppStateContainer.getWindowStateById(windowId)?.also { wbState ->
            if (path != null) {
                wbState.activeWbKey?.also { wbKey ->
                    this.saveWorkbook(wbKey, path, windowId)
                }
            }
        }
    }

    override fun loadWorkbook(path: Path?, windowId: String) {
        if (path != null) {
            val request = LoadWorkbookRequest(PPaths.get(path), windowId)
            loadWbAction.loadWorkbook(request)
        }
    }

    override fun openLoadFileDialog(windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            val windowState = it.value
            windowState.loadDialogState = windowState.loadDialogState.setOpen(true)
        }
    }

    override fun closeLoadFileDialog(windowId: String) {
        subAppStateContainer.getWindowStateMsById(windowId)?.also {
            val windowState = it.value
            windowState.loadDialogState = windowState.loadDialogState.setOpen(false)
        }
    }

    override fun createNewWorkbook(windowId: String) {
        val req = CreateNewWorkbookRequest(windowId = windowId, wbName = null)
        newWbAct.createNewWb(req)
    }

    override fun closeWorkbook(workbookKey: WorkbookKey, windowId: String) {
        subAppStateContainer.getWbState(workbookKey)?.also { wbState ->
            val req = CloseWorkbookRequest(
                wbKey = workbookKey,
                windowId = windowId
            )
            closeWbAction.closeWb(req)
        }
    }
}
