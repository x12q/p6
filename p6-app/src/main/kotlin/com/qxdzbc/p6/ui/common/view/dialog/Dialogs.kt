package com.qxdzbc.p6.ui.common.view.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.*

object Dialogs {
    /**
     * For renaming
     */
    @Composable
    fun RenameDialog(
        title: String = "title",
        initName: String = "",
        onOk: (newName: String) -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
    ) {
        SingleTextFieldDialog(
            title = title,
            text = initName,
            selectAll = true,
            onOk = onOk,
            onCancel = onCancel,
            onCloseRequest = onCloseRequest
        )
    }

    /**
     * This Composable is a Dialog with:
     * - a single-line text title. If the title is empty, the title box will not be rendered
     * - a single-line non-editable text field
     * - an Ok button
     * - a Cancel button
     * This should be used to displayed very short text dialog
     */
    @Composable
    fun SingleTextDialog(
        title: String = "",
        text: String = "default text",
        onOk: () -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
    ) {
        MDialog(
            size = DpSize(300.dp, 115.dp),
            title = title,
            onCloseRequest = onCloseRequest,
        ) {
            MBox(modifier = Modifier.fillMaxSize().padding(start = 15.dp, end = 15.dp)) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    BorderBox(
                        style = BorderStyle.NONE,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        BasicText(
                            text = text,
                            modifier = Modifier.align(Alignment.Center).padding(bottom = 20.dp, top = 14.dp),
                        )
                    }
                    MBox(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 5.dp)) {
                        OkCancel(
                            onOk = { onOk() },
                            onCancel = onCancel
                        )
                    }
                }
            }
        }
    }

    /**
     * This Composable is a Dialog with:
     * - a single-line editable text field
     * - an Ok button
     * - a Cancel button
     */
    @Composable
    fun SingleTextFieldDialog(
        title: String = "",
        text: String = "",
        selectAll: Boolean = false,
        onOk: (newText: String) -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
    ) {
        val currentText = rms(text)
        val fq = remember { FocusRequester() }

        MDialog(
            size = DpSize(300.dp, 115.dp),
            title = title,
            onCloseRequest = onCloseRequest,
        ) {
            MBox(modifier = Modifier.fillMaxSize().padding(start = 15.dp, end = 15.dp)) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    MBox(modifier = Modifier.fillMaxWidth().height(30.dp)) {
                        SingleLineInputText(
                            text = currentText.value,
                            onValueChange = {
                                currentText.value = it
                            },
                            modifier = Modifier.focusRequester(fq).align(Alignment.Center).fillMaxWidth(),
                            selectAll = selectAll,
                        )
                    }
                    MBox(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp)) {
                        OkCancel(
                            onOk = { onOk(currentText.value) },
                            onCancel = onCancel
                        )
                    }
                }
            }
        }
        DisposableEffect(Unit) {
            fq.requestFocus()
            onDispose { }
        }
    }

    @Composable
    fun DirectoryDialog(
        title: String = "",
        initText: String = "",
        textFq: FocusRequester? = null,
        onOk: (text: String) -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
        openBrowserAndUpdateText: (text: Ms<String>) -> Unit = {},
    ) {
        val tMs = rms(initText)
        var t by tMs
        SingleItemDialog(
            title = title,
            size = DpSize(300.dp, 115.dp),
            onOk = {
                onOk(t)
            },
            onCancel = onCancel,
            onCloseRequest = onCloseRequest,
        ) {
            MBox(modifier = Modifier.fillMaxWidth()) {
                MBox(modifier = Modifier.align(Alignment.Center).width(230.dp)) {
                    DirTextField(text = t, onValueChange = {
                        t = it
                    }, onClickBrowseButton = {
                        openBrowserAndUpdateText(tMs)
                    },
                        textFq = textFq
                    )
                }
            }
        }
    }

    @Composable
    fun SingleItemDialog(
        title: String = "",
        onOk: () -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
        size: DpSize = DpSize(100.dp, 100.dp),
        item: @Composable () -> Unit,
    ) {
        MDialog(
            size = size,
            title = title,
            onCloseRequest = onCloseRequest,
        ) {
            MBox(modifier = Modifier.fillMaxSize().padding(start = 15.dp, end = 15.dp)) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    MBox(modifier = Modifier.fillMaxWidth()) {
                        item()
                    }
                    MBox(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp, bottom = 0.dp)) {
                        OkCancel(
                            onOk = onOk,
                            onCancel = onCancel
                        )
                    }
                }
            }
        }
    }
}

