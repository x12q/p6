package com.qxdzbc.p6.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.err.ErrMsg
import com.qxdzbc.p6.app.err.ErrorType
import com.qxdzbc.p6.ui.common.view.dialog.error.ErrorDialogWithStackTrace
import com.qxdzbc.p6.ui.document.workbook.WorkbookView
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBar
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.status_bar.StatusBar
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RpcStatusDetailDialog
import com.qxdzbc.p6.ui.window.tool_bar.ToolBar
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBar


@Composable
fun InnerWindowView(
    oState: OuterWindowState,
    windowActionTable: WindowActionTable,
    windowAction: WindowAction,
) {
    val state: WindowState = oState.innerWindowState
    val activeWbStateMs = state.activeWbStateMs
    val oddityContainerMs = state.errorContainerMs
    val cs = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Surface {
            WindowFrame(
                menu = {
                    ToolBar(
                        windowId = state.id,
                        state = state.toolBarState,
                        action = windowAction.toolBarAction
                    )
                },
                formulaBar = {
                    FormulaBar(state = state.formulaBarState)
                },
                workbookTab = {
                    WorkbookTabBar(
                        state = state.wbTabBarState,
                        action = windowActionTable.wbTabBarAction
                    )
                },
                workbookView = {
                    MBox {
                        if (activeWbStateMs != null) {
                            WorkbookView(
                                wbState = activeWbStateMs   ,
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
