package com.qxdzbc.p6.ui.app.cell_editor.in_cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.key_event.PKeyEvent.Companion.toPKeyEvent
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.common.view.UseP6TextSelectionColor
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.common.p6R

/**
 * Note: the editor should be white, has a border identical to cursor border
 */
@Composable
fun CellEditorView(
    state: CellEditorState,
    action: CellEditorAction,
    isFocused:Boolean,
    defaultSize: DpSize,
    modifier: Modifier = Modifier
) {
    val fc = remember { FocusRequester() }
    val sizeMod = if (state.isActive) {
        Modifier.defaultMinSize(defaultSize.width, defaultSize.height)
    } else {
        Modifier.size(0.dp, 0.dp)
    }
    MBox(
        modifier = modifier
            .then(sizeMod)
            .background(Color.White)
            .then(p6R.border.mod.cursorBorder)
    ) {
        UseP6TextSelectionColor {
            BasicTextField(
                value = state.displayTextField,
                onValueChange = {
                    action.updateTextField(it)
                },
                modifier = Modifier
                    .focusRequester(fc)
                    .width(IntrinsicSize.Min)
                    .onPreviewKeyEvent {
                        action.handleKeyboardEvent(it.toPKeyEvent())
                    }
            )
        }
    }
    SideEffect {
        if(isFocused){
            fc.requestFocus()
        }
    }
}

fun main(){
    testApp {

    }
}
