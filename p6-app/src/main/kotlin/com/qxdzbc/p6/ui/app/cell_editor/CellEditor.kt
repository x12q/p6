package com.qxdzbc.p6.ui.app.cell_editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.common.key_event.P6KeyEvent.Companion.toP6KeyEvent
import com.qxdzbc.p6.ui.theme.UseP6TextSelectionColor
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorFocusStatePerWindow
import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * Note: the editor should be white, has a border identical to cursor border
 */
@Composable
fun CellEditor(
    state: CellEditorState,
    action: CellEditorAction,
    focusState:CursorFocusStatePerWindow,
    size: DpSize = DpSize(400.dp,50.dp),
    modifier: Modifier = Modifier
) {
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
            .border(2.dp, P6Theme.color.ws.cursorColor)

    ) {
        UseP6TextSelectionColor {
            BasicTextField(
                value = state.displayTextField,
                onValueChange = {
                    action.changeTextField(it)
                },
                modifier = Modifier
                    .then(sizeMod)
                    .padding(5.dp)
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

