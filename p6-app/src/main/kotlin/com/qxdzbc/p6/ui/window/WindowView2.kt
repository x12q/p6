package com.qxdzbc.p6.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.common.utils.CoroutineUtils
import com.qxdzbc.p6.app.common.utils.Loggers
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.file_dialog.FileDialog
import com.qxdzbc.p6.ui.window.menu.WindowMenu
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener


@Composable
fun WindowView2(
    oState: OuterWindowState,
    windowActionTable: WindowActionTable,
    windowAction: WindowAction,
) {
    val state = oState.innerWindowState
    val windowState: WindowState = state
    val executionScope = rememberCoroutineScope()
    val launchOnMain = remember { CoroutineUtils.makeLaunchOnMain(executionScope) }
    Loggers.renderLogger.debug("render window view")
    Window(
        onCloseRequest = {
            windowAction.onCloseWindowRequest(state.id)
        },
        title = state.windowTitle,
        onPreviewKeyEvent = {
            false
        },
    ) {
        LaunchedEffect(Unit) {
            window.addWindowFocusListener(object : WindowFocusListener {
                override fun windowGainedFocus(e: WindowEvent?) {
                    windowAction.setActiveWindow(state.id)
                }
                override fun windowLostFocus(e: WindowEvent?) {}
            })
        }
        Column(modifier= Modifier.fillMaxSize()){
            WindowMenu(
                fileMenuAction = windowActionTable.fileMenuAction,
                codeMenuAction = windowActionTable.codeMenuAction,
                windowState = windowState,
            )
            InnerWindowView3(oState, windowActionTable, windowAction)
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
                        windowAction.loadWorkbook(path, state.id)
                        windowAction.closeLoadFileDialog(state.id)
                    }
                })
        }

        if (windowState.openCommonFileDialog) {
            FileDialog("Select file", isLoad = true, onResult = { path ->
                windowAction.setCommonFileDialogResult(path, state.id)
                windowAction.closeCommonFileDialog(state.id)
            })
        }
    }
}
