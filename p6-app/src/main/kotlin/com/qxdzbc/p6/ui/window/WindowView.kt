package com.qxdzbc.p6.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.common.utils.CoroutineUtils
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.file_dialog.FileDialog2
import com.qxdzbc.p6.ui.window.menu.WindowMenu
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener


@Composable
fun WindowView(
    state: OuterWindowState,
    windowActionTable: WindowActionTable,
    windowAction: WindowAction,
) {
    val executionScope = rememberCoroutineScope()
    val launchOnMain = remember { CoroutineUtils.makeLaunchOnMain(executionScope) }
    val iState = rememberWindowState(
        placement = WindowPlacement.Floating
    )
    Window(
        state = iState,
        onCloseRequest = {
            windowAction.closeWindow(state)
        },
        onPreviewKeyEvent = {
            false
        },
    ) {
        LaunchedEffect(Unit) {
            window.addWindowFocusListener(object : WindowFocusListener {
                override fun windowGainedFocus(e: WindowEvent?) {
                    windowAction.setActiveWindow(state.windowId)
                }

                override fun windowLostFocus(e: WindowEvent?) {}
            })
        }
        // x: set window title down here to prevent forced push-to-top effect when inner window state change
        window.title = state.windowTitle
        Column(modifier = Modifier.fillMaxSize()) {
            WindowMenu(
                fileMenuAction = windowActionTable.fileMenuAction,
                windowState = state.innerWindowState
            )
            MBox {
                InnerWindowView(state, windowActionTable, windowAction)
            }
        }
        if (state.saveDialogState.isOpen) {
            FileDialog2(
                "Save workbook", false,
                launchScope = executionScope,
                onResult = { path ->
                    launchOnMain {
                        windowAction.saveActiveWorkbook(path, state.windowId)
                        windowAction.closeSaveFileDialog(state.windowId)
                    }
                },
                onCancel = {
                    windowAction.closeSaveFileDialog(state.windowId)
                },
                makeSavePath = windowAction
            )
        }

        if (state.loadDialogState.isOpen) {
            FileDialog2("Open workbook", true,
                launchScope = executionScope,
                onResult = { path ->
                    launchOnMain {
                        windowAction.loadWorkbook(path, state.windowId)
                        windowAction.closeLoadFileDialog(state.windowId)
                    }
                },
                onCancel = {
                    windowAction.closeLoadFileDialog(state.windowId)
                },
                makeSavePath = windowAction
            )
        }

        if (state.openCommonFileDialog) {
            FileDialog2("Select file",
                isLoad = true,
                launchScope = executionScope,
                defaultFileFilter = null,
                onResult = { path ->
                    windowAction.setCommonFileDialogResult(path, state.windowId)
                    windowAction.closeCommonFileDialog(state.windowId)
                },
                onCancel = {
                    windowAction.closeCommonFileDialog(state.windowId)
                },
                makeSavePath = windowAction
            )
        }
    }
}
