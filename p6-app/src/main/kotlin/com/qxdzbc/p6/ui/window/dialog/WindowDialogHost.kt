package com.qxdzbc.p6.ui.window.dialog

import androidx.compose.runtime.Composable
import com.qxdzbc.p6.ui.window.dialog.ask_to_save_dialog.AskSaveDialog
import com.qxdzbc.p6.ui.window.state.WindowState


@Composable
fun WindowDialogHost(
    windowState: WindowState,
    action: WindowDialogHostAction
) {
    val state: WindowDialogHostState = windowState.dialogHostState
    if (state.isAskSaveOpen) {
        AskSaveDialog(
            windowState.activeWbKey?.name,
            onCancel = {
                action.askSaveDialogAction.onCancel(
                    windowState.id
                )
            },
            onDontSave = {
                action.askSaveDialogAction.onDontSave(windowState.id)
            },
            onSave = {
                windowState.activeWbPointer.wbKeyMs?.also {
                    action.askSaveDialogAction.onSave(windowState.id,it)
                }
            }
        )
    }
}
