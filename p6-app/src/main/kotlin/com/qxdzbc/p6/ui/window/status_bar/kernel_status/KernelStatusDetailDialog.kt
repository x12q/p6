package com.qxdzbc.p6.ui.window.status_bar.kernel_status

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
import com.qxdzbc.p6.app.common.utils.Utils.toYN
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.MButton
import com.qxdzbc.p6.ui.common.view.NotEditableTextField
import com.qxdzbc.p6.ui.common.view.UseP6TextSelectionColor
import com.qxdzbc.p6.ui.common.view.dialog.MDialog
import com.qxdzbc.p6.ui.window.status_bar.DetailEntry

@Composable
fun KernelStatusDetailDialog(
    state: KernelStatusItemState,
    onClickClose: () -> Unit = {}
) {
    val s = state
    MDialog(title = "Kernel status", onCloseRequest = onClickClose, size = DpSize(300.dp, 330.dp)) {
        MBox(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {
                MBox(modifier = Modifier.weight(1.0F)) {
                    Column() {
                        Column {
                            UseP6TextSelectionColor {
                                if(!s.kernelIsRunning){
                                    NotEditableTextField("* Kernel is not running")
                                }else{
                                    NotEditableTextField("* Kernel is running: ${s.kernelIsRunning.toYN()}")
                                    DetailEntry("* Kernel is under management: ${s.kernelIsUnderManagement.toYN()}",)
                                    if (s.kernelIsUnderManagement) {
                                        DetailEntry("* Kernel launch command: ${s.kernelCommand}",)
                                        DetailEntry("* Connection file: ${s.connectionFilePath}",)
                                        DetailEntry("* Kernel process id: ${s.kernelProcessId}",)
                                    }
                                    s.shellPort?.also {
                                        DetailEntry("* Shell port: ${it}",)
                                    }

                                    s.ioPubPort?.also {
                                        DetailEntry("* IO Pub port: ${it}",)
                                    }

                                    s.stdinPort?.also {
                                        DetailEntry("* Stdin port: ${it}",)
                                    }

                                    s.heartBeatPort?.also {
                                        DetailEntry("* Heart beat port: ${it}",)
                                    }

                                    s.ip?.also {
                                        DetailEntry("* ip: ${it}",)
                                    }
                                }
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
