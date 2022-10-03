package com.qxdzbc.p6.ui.app.cell_editor

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
import com.qxdzbc.p6.ui.common.view.UseP6TextSelectionColor
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.common.p6R

/**
 * Note: the editor should be white, has a border identical to cursor border
 */
@Composable
fun CellEditorView(
    state: CellEditorState,
    action: CellEditorAction,
    isFocused:Boolean,
    size: DpSize = DpSize(400.dp,50.dp),
    modifier: Modifier = Modifier
) {
    val fc = remember { FocusRequester() }
    val sizeMod = if (state.isActive) {
        Modifier.widthIn(min=size.width).heightIn(min = size.height).width(IntrinsicSize.Min)
    } else {
        Modifier.size(0.dp, 0.dp)
    }

    val boxSizeMod = if (state.isActive) {
        Modifier
    } else {
        Modifier.size(0.dp, 0.dp)
    }

    LaunchedEffect(isFocused) {
        if(isFocused){
            fc.requestFocus()
        }
    }

    MBox(
        modifier = modifier
            .then(boxSizeMod)
            .background(Color.White)
            .then(p6R.border.mod.cursorBorder)

    ) {
        UseP6TextSelectionColor {
            BasicTextField(
                value = state.displayTextField,
                onValueChange = {
                    it.composition
                    action.updateTextField(it)
                },
                modifier = Modifier
                    .then(sizeMod)
                    .then(p6R.padding.mod.stdTextFieldPadding)
                    .focusRequester(fc)
                    .onPreviewKeyEvent {
                        action.handleKeyboardEvent(it.toPKeyEvent())
                    }
            )
        }
    }
}

