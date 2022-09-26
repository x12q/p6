package com.qxdzbc.p6.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.oddity.ErrMsg
import com.qxdzbc.p6.app.oddity.ErrorType
import com.qxdzbc.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace
import com.qxdzbc.p6.ui.document.workbook.WorkbookView
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBar
import com.qxdzbc.p6.ui.window.kernel_dialog.ConnectToKernelDialog
import com.qxdzbc.p6.ui.window.kernel_dialog.start_kernel_dialog.StartKernelDialog
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.status_bar.StatusBar
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusDetailDialog
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RpcStatusDetailDialog
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun InnerWindowView(
    oState: OuterWindowState,
    windowActionTable: WindowActionTable,
    windowAction: WindowAction,
) {
    val state: WindowState = oState.innerWindowState
    val activeWbStateMs = state.activeWbStateMs
    val oddityContainerMs = state.errorContainerMs
    val executionScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Surface {
            WindowFrame(
                menu = {},
                formulaBar = {
                    FormulaBar(state = state.formulaBarState)
                },
                workbookTab = {
                    WorkbookTabBarView(
                        state = state.wbTabBarState,
                        wbTabBarActions = windowActionTable.wbTabBarAction
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
                    StatusBar(state.statusBarState)
                }
            )
        }
    }

    if (oddityContainerMs.value.isNotEmpty()) {
        for (bugMsg: ErrMsg in oddityContainerMs.value.errList) {
            ErrorDialogWithStackTrace(
                errMsg = bugMsg,
                onOkClick = {
                    when (bugMsg.type) {
                        ErrorType.FATAL -> {
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

    if (state.showStartKernelDialogState.isShowing) {
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
    if (state.showConnectToKernelDialogState.isShowing) {
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

    if (state.statusBarState.kernelStatusItemState.detailIsShown) {
        KernelStatusDetailDialog(state = state.statusBarState.kernelStatusItemState,
            onClickClose = {
                state.statusBarState.kernelStatusItemState =
                    state.statusBarState.kernelStatusItemState.hideDetail()
            }
        )
    }
    if (state.statusBarState.rpcServerStatusState.isShowingDetail) {
        RpcStatusDetailDialog(
            state = state.statusBarState.rpcServerStatusState,
            onClickClose = {
                state.statusBarState.rpcServerStatusState =
                    state.statusBarState.rpcServerStatusState.hideDetail()
            }
        )
    }
}

