package com.emeraldblast.p6.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.emeraldblast.p6.app.common.utils.CoroutineUtils
import com.emeraldblast.p6.app.common.utils.Loggers
import com.emeraldblast.p6.app.common.utils.PPath
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.oddity.OddMsg
import com.emeraldblast.p6.app.oddity.OddityType
import com.emeraldblast.p6.ui.action_table.WindowActionTable
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace
import com.emeraldblast.p6.ui.document.workbook.WorkbookView
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.ui.window.file_dialog.FileDialog
import com.emeraldblast.p6.ui.window.formula_bar.FormulaBar
import com.emeraldblast.p6.ui.window.kernel_dialog.ConnectToKernelDialog
import com.emeraldblast.p6.ui.window.kernel_dialog.start_kernel_dialog.StartKernelDialog
import com.emeraldblast.p6.ui.window.menu.WindowMenu
import com.emeraldblast.p6.ui.window.state.WindowState
import com.emeraldblast.p6.ui.window.status_bar.StatusBar
import com.emeraldblast.p6.ui.window.status_bar.kernel_status.KernelStatusDetailDialog
import com.emeraldblast.p6.ui.window.status_bar.rpc_status.RpcStatusDetailDialog
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun makeWindowTitle(workbookKey: WorkbookKey?): String {
    if (workbookKey != null) {
        return "P6 - ${workbookKey.name}"
    } else {
        return "P6"
    }
}

@Composable
fun WindowView(
    state: WindowState,
    windowActionTable: WindowActionTable,
    windowAction: WindowAction,
) {
    val windowState: WindowState = state
    val activeWbStateMs = windowState.activeWorkbookStateMs
    val oddityContainerMs = windowState.oddityContainerMs
    val executionScope = rememberCoroutineScope()
    val launchOnMain = remember { CoroutineUtils.makeLaunchOnMain(executionScope) }
    Loggers.renderLogger.debug("render window view")
    Window(
        onCloseRequest = {
            windowAction.onCloseWindowRequest(state.id)
        },
        title = makeWindowTitle(activeWbStateMs?.value?.wb?.key),
        onPreviewKeyEvent = {
            false
        },
    ) {
        Surface {
            WindowFrame(
                menu = {
                    WindowMenu(
                        fileMenuAction = windowActionTable.getFileMenuAction(),
                        codeMenuAction = windowActionTable.getCodeMenuAction(),
                        windowState = windowState,
                    )
                },
                formulaBar = {
                    FormulaBar(state = windowState.formulaBarState)
                },
                workbookTab = {
                    WorkbookTabBarView(
                        state = windowState.workbookTabBarState,
                        wbTabBarActions = windowActionTable.getWbTabBarAction()
                    )
                },
                workbookView = {
                    MBox {
                        if (activeWbStateMs != null) {
                            WorkbookView(
                                wbState = activeWbStateMs.value,
                                wbActionTable = windowActionTable.workbookActionTable,
                                focusState = state.focusState
                            )
                        }
                    }
                },
                statusBar = {
                    StatusBar(windowState.statusBarState)
                }
            )
        }

        if (windowState.saveDialogState.isOpen) {
            FileDialog("Save workbook", false,
                onResult = { path ->
                    launchOnMain {
                        windowAction.saveActiveWorkbook(path, state.id)
                        windowAction.closeSaveFileDialog(state.id)
                    }
                })
        }

        if (windowState.loadDialogState.isOpen) {
            FileDialog("Open workbook", true,
                onResult = { path ->
                    launchOnMain {
                        windowAction.loadWorkbook(path?.let { PPath(it) }, state.id)
                        windowAction.closeLoadFileDialog(state.id)
                    }
                })
        }

        if (oddityContainerMs.value.isNotEmpty()) {
            for (bugMsg: OddMsg in oddityContainerMs.value.oddList) {
                ErrorDialogWithStackTrace(
                    oddMsg = bugMsg,
                    onOkClick = {
                        when (bugMsg.type) {
                            OddityType.FATAL -> {
                                oddityContainerMs.value = oddityContainerMs.value.remove(bugMsg)
                                println("Kill app when encounter fatal error")
                                windowAction.onFatalError()
                            }
                            else -> oddityContainerMs.value = oddityContainerMs.value.remove(bugMsg)
                        }
                    },
                )
            }
        }

        if (windowState.showStartKernelDialogState.isShowing) {
            StartKernelDialog(
                onOk = {
                    executionScope.launch(Dispatchers.IO) {
                        windowAction.startKernelAndServices(it)
                        windowAction.closeDialogToStartKernel(state.id)
                    }
                },
                onCancel = {
                    windowAction.closeDialogToStartKernel(state.id)
                },
                openBrowserToUpdatePath = { updateTarget ->
                    windowAction.openCommonFileBrowserAndUpdatePath(updateTarget, executionScope, state.id)
                }
            )
        }
        if (windowState.showConnectToKernelDialogState.isShowing) {
            ConnectToKernelDialog(
                onOk = { path, content ->
                    executionScope.launch {
                        if (path != null) {
                            windowAction.connectToKernelUsingConnectionFilePath(path)
                        } else if (content != null) {
                            windowAction.connectToKernelUsingConnectionFileContent(content)
                        }
                    }
                    windowAction.closeDialogToConnectToKernel(state.id)
                },
                openFileBrowserAndUpdatePath = {
                    windowAction.openCommonFileBrowserAndUpdatePath(it, executionScope, state.id)
                },
                onCancel = {
                    windowAction.closeDialogToConnectToKernel(state.id)
                }
            )
        }
        if (windowState.openCommonFileDialog) {
            FileDialog("Select file", isLoad = true, onResult = { path ->
                windowAction.setCommonFileDialogResult(path, state.id)
                windowAction.closeCommonFileDialog(state.id)
            })
        }
        if (windowState.statusBarState.kernelStatusItemState.detailIsShown) {
            KernelStatusDetailDialog(state = windowState.statusBarState.kernelStatusItemState,
                onClickClose = {
                    windowState.statusBarState.kernelStatusItemState =
                        windowState.statusBarState.kernelStatusItemState.hideDetail()
                }
            )
        }
        if (windowState.statusBarState.rpcServerStatusState.isShowingDetail) {
            RpcStatusDetailDialog(
                state = windowState.statusBarState.rpcServerStatusState,
                onClickClose = {
                    windowState.statusBarState.rpcServerStatusState =
                        windowState.statusBarState.rpcServerStatusState.hideDetail()
                }
            )
        }
    }
}
