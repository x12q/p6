package com.qxdzbc.p6.ui.document.worksheet.cursor.state

import com.qxdzbc.p6.app.common.focus_requester.P6FocusRequester

/**
 * Each window has one of this.
 */
interface CursorFocusStatePerWindow {

    val isCursorFocused:Boolean
    val cursorFocusRequester: P6FocusRequester
    fun setCursorFocus(i:Boolean):CursorFocusStatePerWindow
    fun focusOnCursor(): CursorFocusStatePerWindow
    fun freeFocusOnCursor(): CursorFocusStatePerWindow

    val isEditorFocused:Boolean
    fun setCellEditorFocus(i:Boolean):CursorFocusStatePerWindow
    val editorFocusRequester: P6FocusRequester
    fun focusOnEditor(): CursorFocusStatePerWindow
    fun freeFocusOnEditor(): CursorFocusStatePerWindow
}
