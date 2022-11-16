package com.qxdzbc.p6.ui.window.focus_state


import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.common.focus_requester.FocusRequesterWrapper
import org.mockito.Mockito.*
import kotlin.test.*

class SingleWindowFocusStateImpTest {
    lateinit var f : SingleWindowFocusStateImp
    lateinit var mockCursorFR:FocusRequesterWrapper
    lateinit var mockEditorFR:FocusRequesterWrapper
    @BeforeTest
    fun b(){
        mockCursorFR = mock(FocusRequesterWrapper::class.java)
        mockEditorFR = mock(FocusRequesterWrapper::class.java)
        f = SingleWindowFocusStateImp(
            isCursorFocusedMs = false.toMs(),
            isEditorFocusedMs = false.toMs(),
            cursorFocusRequester = mockCursorFR,
            editorFocusRequester = mockEditorFR
        )
    }
    @Test
    fun focusOnCursor() {
        val f2 = f.focusOnCursor()
        verify(mockCursorFR, times(1)).requestFocus()
    }

    @Test
    fun freeFocusOnCursor() {
        val f2 = f.freeFocusOnCursor()
        verify(mockCursorFR, times(1)).freeFocus()
    }

    @Test
    fun focusOnEditor() {
        val f2 = f.focusOnEditor()
        verify(mockEditorFR, times(1)).requestFocus()
    }

    @Test
    fun freeFocusOnEditor() {
        val f2 = f.focusOnEditor()
        verify(mockEditorFR, times(1)).requestFocus()
    }
}
