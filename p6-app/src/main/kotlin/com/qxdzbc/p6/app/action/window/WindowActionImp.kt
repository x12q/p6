package com.qxdzbc.p6.app.action.window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ApplicationScope
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowAction
import com.qxdzbc.p6.app.action.window.close_window.CloseWindowAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey


import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.kernel.KernelAction
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.nio.file.Path
import javax.inject.Inject

class WindowActionImp @Inject constructor(
    private val appScope: ApplicationScope?,
    private val appStateMs: Ms<AppState>,
    private val kernelAction: KernelAction,
    private val closeWbAction: CloseWorkbookAction,
    private val stateContMs: Ms<SubAppStateContainer>,
    private val newWbAct: CreateNewWorkbookAction,
    private val saveWbAction: SaveWorkbookAction,
    private val loadWbAction: LoadWorkbookAction,
    private val setActiveWdAct: SetActiveWindowAction,
    private val closeWindowAct: CloseWindowAction,
) : WindowAction,
    CloseWindowAction by closeWindowAct,
    SetActiveWindowAction by setActiveWdAct,
    KernelAction by kernelAction,
    SaveWorkbookAction by saveWbAction {

    private var stateCont by stateContMs
    private var appState by appStateMs

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
        appState.getWindowStateMsById(windowId)?.also {
            it.value = it.value.setCommonFileDialogJob(job)
        }

    }

    override fun closeCommonFileDialog(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            it.value = it.value.removeCommonFileDialogJob()
        }

    }

    override fun setCommonFileDialogResult(path: Path?, windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            it.value.commonFileDialogJob?.complete(path)
        }
    }

    override fun openDialogToStartKernel(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            it.value.showStartKernelDialogState = it.value.showStartKernelDialogState.show()
        }
    }

    override fun closeDialogToStartKernel(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            it.value.showStartKernelDialogState = it.value.showStartKernelDialogState.hide()
        }
    }

    override fun openDialogToConnectToKernel(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            it.value.showConnectToKernelDialogState = it.value.showConnectToKernelDialogState.show()
        }
    }

    override fun closeDialogToConnectToKernel(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            it.value.showConnectToKernelDialogState = it.value.showConnectToKernelDialogState.hide()
        }
    }

    override fun onFatalError() {
        appScope?.exitApplication()
    }

    override fun saveActiveWorkbook(path: Path?, windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            if (path != null) {
                val wbKey = it.value.activeWbKey
                if (wbKey != null) {
                    saveWorkbook(wbKey, path, windowId)
                }
            }
        }
    }

    override fun openSaveFileDialog(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            val windowState = it.value
            windowState.saveDialogStateMs.value = windowState.saveDialogState.setOpen(true)
        }
    }

    override fun closeSaveFileDialog(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            val windowState = it.value
            windowState.saveDialogStateMs.value = windowState.saveDialogState.setOpen(false)
        }
    }

    override fun loadWorkbook(path: Path?, windowId: String) {
        if (path != null) {
            val request = LoadWorkbookRequest(PPaths.get(path), windowId)
            loadWbAction.loadWorkbook(request)
        }
    }

    override fun openLoadFileDialog(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            val windowState = it.value
            windowState.loadDialogState = windowState.loadDialogState.setOpen(true)
        }
    }

    override fun closeLoadFileDialog(windowId: String) {
        appState.getWindowStateMsById(windowId)?.also {
            val windowState = it.value
            windowState.loadDialogState = windowState.loadDialogState.setOpen(false)
        }
    }

    override fun createNewWorkbook(windowId: String) {
        val req = CreateNewWorkbookRequest(windowId = windowId, wbName = null)
        newWbAct.createNewWb(req)
    }

    override fun closeWorkbook(workbookKey: WorkbookKey, windowId: String) {
        val req = CloseWorkbookRequest(
            wbKey = workbookKey,
            windowId = windowId
        )
        closeWbAction.closeWb(req)
    }
}
