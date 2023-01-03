package com.qxdzbc.p6.ui.common.view.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.*

object Dialogs {
    /**
     * A draggable dialog that host a single editable text field, an Ok button, and a cancel button. For renaming things
     */
    @Composable
    fun RenameDialog(
        title: String = "title",
        initName: String = "",
        onOk: (newName: String) -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
    ) {
        SingleTextFieldDraggableDialog(
            title = title,
            text = initName,
            selectAll = true,
            onOk = onOk,
            onCancel = onCancel,
            onCloseRequest = onCloseRequest
        )
    }

    /**
     * A Dialog with:
     * - a single-line text title.
     * - a single-line non-editable text field.
     * - an Ok button.
     * - a Cancel button.
     * This should be used to displaying a short message.
     */
    @Composable
    fun MessageDialog(
        title: String = "",
        text: String = "default text",
        onOk: () -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
    ) {
        DraggableDialog(
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
    fun SingleTextFieldDraggableDialog(
        title: String = "",
        text: String = "",
        selectAll: Boolean = false,
        onOk: (newText: String) -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
    ) {
        val currentText = rms(text)
        val fq = remember { FocusRequester() }

        DraggableDialog(
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

    /**
     * A dialog containing:
     * - a text field showing a directory
     * - a button to open a file chooser to update the directory text
     * - an Ok button and a Cancel button
     */
    @Composable
    fun DirectoryDraggableDialog(
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
        SingleItemDraggableDialog(
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
    fun SingleItemDraggableDialog(
        title: String = "",
        onOk: () -> Unit = {},
        onCancel: () -> Unit = {},
        onCloseRequest: () -> Unit = onCancel,
        size: DpSize = DpSize(100.dp, 100.dp),
        item: @Composable () -> Unit,
    ) {
        DraggableDialog(
            size = size,
            title = title,
            onCloseRequest = onCloseRequest,
        ) {
            MBox(modifier = Modifier.fillMaxSize().padding(start = 15.dp, end = 15.dp)) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    MBox(modifier = Modifier.weight(1.0f)) {
                        item()
                    }
                    MBox(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp, bottom = 10.dp)) {
                        OkCancel(
                            boxModifier = Modifier.align(Alignment.Center),
                            onOk = onOk,
                            onCancel = onCancel
                        )
                    }
                }
            }
        }
    }

    /**
     * An empty dialog that can be moved around by dragging
     */
    @Composable
    fun DraggableDialog(
        title: String = "",
        size: DpSize = DpSize(100.dp, 100.dp),
        onCloseRequest: () -> Unit = {},
        content: @Composable BoxScope.() -> Unit = {},
    ) {

        DraggableDialog(
            title = {
                if (title.isNotEmpty()) {
                    BasicText(title, modifier = Modifier.align(Alignment.Center).padding(top = 10.dp, bottom = 5.dp))
                }
            },
            size, onCloseRequest, content
        )
    }

    /**
     * A custom, empty dialog that can be moved around by dragging
     */
    @Composable
    fun DraggableDialog(
        title: @Composable BoxScope.() -> Unit = {},
        size: DpSize = DpSize(100.dp, 100.dp),
        onCloseRequest: () -> Unit = {},
        content: @Composable BoxScope.() -> Unit = {},
    ) {
        Dialog(
            onCloseRequest = onCloseRequest,
            state = DialogState(size = size),
            resizable = false, title = "",
            undecorated = true,
        ) {

            Surface {
                WindowDraggableArea(modifier = Modifier.fillMaxSize()) {}
                Column(modifier = Modifier.fillMaxSize()) {
                    BorderBox(style = BorderStyle.NONE, modifier = Modifier.fillMaxWidth()) {
                        title()
                    }
                    MBox(modifier = Modifier.fillMaxWidth().weight(1.0F)) {
                        content()
                    }
                }
            }
        }
    }
}

