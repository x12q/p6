package com.qxdzbc.p6.ui.window.tool_bar.text_size_selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.view.SingleLineInputText
import com.qxdzbc.p6.ui.window.tool_bar.ToolBarDropDownMenuWithButton
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action.TextSizeSelectorAction
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action.TextSizeSelectorActionDoNothing
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorState
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorStateImp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextSizeSelector(
    windowId: String,
    state: TextSizeSelectorState,
    action: TextSizeSelectorAction,
    items: List<Int> = listOf(5, 7, 10, 13, 12, 15, 17, 20, 22, 25, 27, 30, 40, 50),
) {
    val headerText = state.headerText
    val expandedMs = rms(false)
    ToolBarDropDownMenuWithButton(
        expanded = expandedMs.value,
        header = {
            SingleLineInputText(
                text = headerText,
                isBordered = false,
                onValueChange = {
                    action.setHeaderTextOfTextSizeSelector(windowId, it)
                },
                modifier = Modifier
                    .size(width = 40.dp, height = 30.dp)
                    .onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                            action.submitManualEdit(windowId, headerText)
                            true
                        } else {
                            false
                        }
                    }
            )
        },
        items = {
            items.forEachIndexed { i, item ->
                DropdownMenuItem(
                    onClick = {
                        action.pickTextSize(windowId, item)
                        expandedMs.value = false
                    }
                ) {
                    Text("$item")
                }
            }
        },
        onDismiss = {
            expandedMs.value = false
        },
        onButtonClick = {
            expandedMs.value = true
        }
    )
}


fun main() = application {
    Window(
        state = WindowState(width = 350.dp, height = 450.dp),
        onCloseRequest = ::exitApplication
    ) {
        var s by rms(1f)
        var text by rms("")
        val fc = remember { FocusRequester() }
        val stateMs = rms(TextSizeSelectorStateImp(headerText = "10"))
        Column {
            TextField(text, onValueChange = {
                text = it
            }, modifier = Modifier.focusRequester(fc))
            Text("$s")
            TextSizeSelector(
                state = stateMs.value,
                windowId = "",
                action = TextSizeSelectorActionDoNothing(),
            )
        }
    }
}
