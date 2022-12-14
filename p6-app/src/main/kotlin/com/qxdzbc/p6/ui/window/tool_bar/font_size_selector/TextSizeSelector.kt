package com.qxdzbc.p6.ui.window.tool_bar.font_size_selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
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
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.SingleLineInputText
import com.qxdzbc.p6.ui.common.view.buttons.DropDownButton

interface TextSizeSelectorInternalState {
    val expanded: Boolean
    fun setExpanded(i: Boolean): TextSizeSelectorInternalState
}

data class TextSizeSelectorInternalStateImp(
    override val expanded: Boolean = false,
) : TextSizeSelectorInternalState {
    override fun setExpanded(i: Boolean): TextSizeSelectorInternalStateImp {
        return this.copy(expanded = i)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextSizeSelector(
    initValue: Float = 10f,
    items: List<Int> = listOf(10,12,15,17,20,22,25,27,30,40,50),
    internalStateMs: Ms<TextSizeSelectorInternalState> = rms(TextSizeSelectorInternalStateImp()),
    onItemPick: (Float) -> Unit = {},
    returnFocus:()->Unit = {}
) {
    var iState by internalStateMs
    var tempText by rms("$initValue")
    BorderBox {
        Row {
            SingleLineInputText(
                text = tempText,
                onValueChange = { newText ->
                    tempText = newText
                },
                modifier = Modifier
                    .size(50.dp,30.dp)
                    .onPreviewKeyEvent {
                    if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                        val newNum = tempText.toFloatOrNull()
                        if(newNum!=null){
                            onItemPick(newNum)
                            returnFocus()
                            true
                        }else{
                            false
                        }
                    } else {
                        false
                    }
                }
            )
            DropDownButton(
                enabled = !iState.expanded,
                modifier = Modifier.size(30.dp),
                onClick = {
                    iState = iState.setExpanded(true)
                }
            )
        }
    }

    DropdownMenu(
        expanded = iState.expanded,
        onDismissRequest = {
            iState = iState.setExpanded(false)
        },
    ) {
        items.forEachIndexed { i, item ->
            DropdownMenuItem(
                onClick = {
                    iState = iState.setExpanded(false)
                    tempText = item.toString()
                    onItemPick(item.toFloat())
                    returnFocus()
                }
            ) {
                Text("$item")
            }
        }
    }
}


fun main() = application {
    Window(
        state = WindowState(width = 350.dp, height = 450.dp),
        onCloseRequest = ::exitApplication
    ) {
        var s by rms(1f)
        var text by rms("")
        val fc = remember{FocusRequester()}
        Column{
            TextField(text,onValueChange={
                text=it
            },modifier = Modifier.focusRequester(fc))
            Text("$s")
            TextSizeSelector(
                onItemPick = {
                    s = it
                },
                returnFocus={
                    fc.requestFocus()
                }
            )
        }
    }
}
