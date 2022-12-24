package com.qxdzbc.p6.ui.window.kernel_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.p6.ui.common.view.DirTextField
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.dialog.Dialogs

@Composable
fun ConnectToKernelDialog(
    onOk: (connectionFilePath: String?, connectionFileJson: String?) -> Unit = { _, _ -> },
    openFileBrowserAndUpdatePath: (text: Ms<String>) -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val fq1 = remember { FocusRequester() }
    val fq2 = remember { FocusRequester() }

    val pathOption = 1
    val contentOption = 2
    var selection by rms(pathOption)


    fun usePath():Boolean = selection == pathOption
    fun useContent():Boolean = selection == contentOption

    val pathMs = rms("")
    var path by pathMs
    var jsonContent by rms("")

    fun selectPath() {
        selection = pathOption
    }

    fun selectContent() {
        selection = contentOption
    }
    LaunchedEffect(selection) {
        if (usePath()) {
            fq1.requestFocus()
        }
        if (useContent()) {
            fq2.requestFocus()
        }
    }
    Dialogs.SingleItemDraggableDialog(
        title = "Connect to a running kernel",
        size = DpSize(300.dp, 350.dp),
        onOk = {
            if (usePath()) {
                onOk(path, null)
            }
            if (useContent()) {
                onOk(null, jsonContent)
            }
        },
        onCancel = onCancel
    ) {
        val color = P6R.color.radioButtonColor()
        MBox(modifier = Modifier) {
            Column {
                MBox {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                            selectPath()
                        }) {
                            RadioButton(
                                selected = usePath(), colors = color, enabled = false, onClick = {}
                            )
                            Text("Use connection file")
                        }
                        MBox {
                            DirTextField(
                                text = path,
                                enabled = usePath(),
                                onValueChange = { path = it },
                                onClickBrowseButton = { openFileBrowserAndUpdatePath(pathMs) },
                                textFq = fq1
                            )
                            if (!usePath()) {
                                // a layer to blur out unselected view
                                MBox(
                                    modifier = Modifier.matchParentSize()
                                        .background(color = Color.White.copy(alpha = 0.7F))
                                        .clickable { selectPath() }
                                )
                            }
                        }
                    }
                }

                MBox {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                selectContent()
                            }
                        ) {
                            RadioButton(
                                selected = useContent(), colors = color, enabled = false, onClick = {}
                            )
                            Text("Use connection file content")
                        }

                        MBox {
                            MBox(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        color = MaterialTheme.colors.onPrimary,
                                        shape = P6R.shape.textFieldShape
                                    )
                                    .fillMaxWidth()
                                    .height(120.dp),
                            ) {
                                BasicTextField(
                                    value = jsonContent,
                                    onValueChange = {
                                        jsonContent = it
                                    },
                                    modifier = Modifier.fillMaxSize().padding(5.dp).focusRequester(fq2),
                                    enabled = useContent(),
                                )
                            }
                            if (!useContent()) {
                                // a layer to blur out unselected view
                                MBox(
                                    modifier = Modifier.matchParentSize()
                                        .background(color = Color.White.copy(alpha = 0.7F))
                                        .clickable { selectContent() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


fun main() {
    P6TestApp {
        ConnectToKernelDialog(

        )
    }
}
