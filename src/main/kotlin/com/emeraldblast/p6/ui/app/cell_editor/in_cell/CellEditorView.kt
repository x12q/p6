package com.emeraldblast.p6.ui.app.cell_editor.in_cell

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
import com.emeraldblast.p6.app.build.DebugFunctions.debug
import com.emeraldblast.p6.app.common.utils.PKeyEvent.Companion.toPKeyEvent
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.common.view.UseP6TextSelectionColor
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState

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
        modifier = Modifier
            .then(modifier)
            .then(sizeMod)
            .background(Color.Red.debug())
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
