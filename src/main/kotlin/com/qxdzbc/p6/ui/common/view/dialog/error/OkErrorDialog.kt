package com.qxdzbc.p6.ui.common.view.dialog.error

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import com.qxdzbc.p6.ui.common.view.MBox
import com.qxdzbc.p6.ui.common.view.OkButton

/**
 * A standard error dialog with a single Ok button
 */
@Composable
fun OkErrorDialog(
    dialogState: DialogState,
    errorBody: @Composable () -> Unit,
    errorTitle: @Composable () -> Unit,
    onOkClick: () -> Unit,
    onDismiss: () -> Unit = onOkClick,
    modifier: Modifier = Modifier,
    resizable: Boolean = true
) {
    BaseErrorDialog(
        dialogState = dialogState,
        onDismiss = onDismiss,
        resizable = resizable,
    ) {
        MBox(modifier = modifier.fillMaxSize().padding(10.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                MBox(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    errorTitle()
                }
                MBox(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1.0F)
                ) {
                    errorBody()
                }
                MBox(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                ) {
                    OkButton(modifier = Modifier.align(Alignment.CenterEnd)) { onOkClick() }
                }
            }
        }
    }
}
