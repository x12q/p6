package com.qxdzbc.p6.ui.document.worksheet.cursor.state

import androidx.compose.ui.focus.FocusRequester

interface CursorFocusState {

    val isCursorFocused:Boolean
    val cursorFocusRequester: FocusRequester
    fun setCursorFocus(i:Boolean):CursorFocusState
    fun focusOnCursor(): CursorFocusState
    fun freeFocusOnCursor(): CursorFocusState

    val isEditorFocused:Boolean
    fun setCellEditorFocus(i:Boolean):CursorFocusState
    val editorFocusRequester: FocusRequester
    fun focusOnEditor(): CursorFocusState
    fun freeFocusOnEditor(): CursorFocusState
}
