package com.qxdzbc.p6.ui.window.status_bar.rpc_status

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.MButton
import com.qxdzbc.p6.ui.common.view.UseP6TextSelectionColor
import com.qxdzbc.p6.ui.common.view.dialog.DraggableDialog
import com.qxdzbc.p6.ui.window.status_bar.DetailEntry

@Composable
fun RpcStatusDetailDialog(
    state: RPCStatusViewState,
    onClickClose: () -> Unit = {}
) {
    val s = state

    DraggableDialog(title = "RPC server status", onCloseRequest = onClickClose, size = DpSize(300.dp, 330.dp)) {
        MBox(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {
                MBox(modifier = Modifier.weight(1.0F)) {
                    Column {
                        UseP6TextSelectionColor {
                            if (s.isRunning) {
                                DetailEntry("* RPC server is running")
                                s.port?.also {
                                    DetailEntry("* Port: ${it}")
                                }
                            } else {
                                DetailEntry("* RPC server is not running")

                            }
                        }
                    }
                }
                MBox(modifier = Modifier.fillMaxWidth()) {
                    MButton(onClick = onClickClose, modifier = Modifier.align(Alignment.CenterEnd)) {
                        BasicText("Close")
                    }
                }
            }

        }
    }
}

