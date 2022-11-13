package com.qxdzbc.p6.ui.document.worksheet.cursor.state

import androidx.compose.ui.focus.FocusRequester
import com.qxdzbc.p6.app.common.focus_requester.FocusRequesterWrapper

interface CursorFocusState {

    val isCursorFocused:Boolean
    val cursorFocusRequester: FocusRequesterWrapper
    fun setCursorFocus(i:Boolean):CursorFocusState
    fun focusOnCursor(): CursorFocusState
    fun freeFocusOnCursor(): CursorFocusState

    val isEditorFocused:Boolean
    fun setCellEditorFocus(i:Boolean):CursorFocusState
    val editorFocusRequester: FocusRequesterWrapper
    fun focusOnEditor(): CursorFocusState
    fun freeFocusOnEditor(): CursorFocusState
}
