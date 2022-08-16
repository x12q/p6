package com.emeraldblast.p6.ui.common.view.dialog.error

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState

/**
 * A basic draggable, undecorated dialog. The content is completely up to the user
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BaseErrorDialog(
    dialogState: DialogState,
    resizable: Boolean = true,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Dialog(
        onCloseRequest = onDismiss,
        state = dialogState,
        undecorated = true,
        resizable = resizable,
        onKeyEvent = {
            if (it.key == Key.Escape) {
                onDismiss()
                true
            } else {
                false
            }
        },
    ) {
        WindowDraggableArea {
            content()
        }
    }
}
