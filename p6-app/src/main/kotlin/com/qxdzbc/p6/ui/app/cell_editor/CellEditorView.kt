package com.qxdzbc.p6.ui.app.cell_editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent.Companion.toP6KeyEvent
import com.qxdzbc.p6.ui.common.view.UseP6TextSelectionColor
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState

/**
 * Note: the editor should be white, has a border identical to cursor border
 */
@Composable
fun CellEditorView(
    state: CellEditorState,
    action: CellEditorAction,
    focusState:CursorFocusState,
    size: DpSize = DpSize(400.dp,50.dp),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit){
        action.focusOnCellEditor()
    }
    LaunchedEffect(focusState.isEditorFocused) {
        if(focusState.isEditorFocused){
            action.focusOnCellEditor()
        }else{
            action.freeFocusOnCellEditor()
        }
    }
    val fc = focusState.editorFocusRequester
    val sizeMod = if (state.isOpen) {
        Modifier.widthIn(min=size.width).heightIn(min = size.height).width(IntrinsicSize.Min)
    } else {
        Modifier.size(0.dp, 0.dp)
    }

    val boxSizeMod = if (state.isOpen) {
        Modifier
    } else {
        Modifier.size(0.dp, 0.dp)
    }

    MBox(
        modifier = modifier
            .then(boxSizeMod)
            .background(Color.White)
            .then(P6R.border.mod.cursorBorder)

    ) {
        UseP6TextSelectionColor {
            BasicTextField(
                value = state.displayTextField,
                onValueChange = {
                    action.changeTextField(it)
                },
                modifier = Modifier
                    .then(sizeMod)
                    .then(P6R.padding.mod.stdTextFieldPadding)
                    .onPreviewKeyEvent {
                        action.handleKeyboardEvent(it.toP6KeyEvent())
                    }
                    .focusRequester(fc.focusRequester)
                    .onFocusChanged {
                        action.setCellEditorFocus(it.isFocused)
                    }
            )
        }
    }
}

