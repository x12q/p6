package com.qxdzbc.p6.ui.window.tool_bar.font_size_selector

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.SingleLineInputText
import com.qxdzbc.p6.ui.common.view.buttons.DropDownButton
import com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.action.TextSizeSelectorAction
import com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.state.TextSizeSelectorState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextSizeSelector(
    windowId:String,
    state: TextSizeSelectorState,
    action: TextSizeSelectorAction,
    items: List<Int> = listOf(10, 12, 15, 17, 20, 22, 25, 27, 30, 40, 50),
) {
    val tempText = state.headerText
    var expanded by rms(false)
    BorderBox {
        Row {
            SingleLineInputText(
                text = tempText,
                onValueChange = {
                    action.setHeaderTextOfTextSizeSelector(windowId, it)
                },
                modifier = Modifier
                    .size(50.dp, 30.dp)
                    .onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                            action.submitManualEdit(windowId, tempText)
                            true
                        } else {
                            false
                        }
                    }
            )
            DropDownButton(
                enabled = !expanded,
                modifier = Modifier.size(30.dp),
                onClick = {
                    expanded = true
                }
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        },
    ) {
        items.forEachIndexed { i, item ->
            DropdownMenuItem(
                onClick = {
                    action.pickItemFromList(windowId, item)
                    expanded = false
                }
            ) {
                Text("$item")
            }
        }
    }
}


//fun main() = application {
//    Window(
//        state = WindowState(width = 350.dp, height = 450.dp),
//        onCloseRequest = ::exitApplication
//    ) {
//        var s by rms(1f)
//        var text by rms("")
//        val fc = remember{FocusRequester()}
//        val stateMs = rms(TextSizeSelectorStateImp(headerText="10"))
//        Column{
//            TextField(text,onValueChange={
//                text=it
//            },modifier = Modifier.focusRequester(fc))
//            Text("$s")
//            TextSizeSelector(
//                state =stateMs.value,
//                onItemPick = {
//                    s = it
//                },
//                returnFocus={
//                    fc.requestFocus()
//                }
//            )
//        }
//    }
//}
