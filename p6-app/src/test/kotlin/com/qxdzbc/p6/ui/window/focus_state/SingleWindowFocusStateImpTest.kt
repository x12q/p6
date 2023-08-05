package com.qxdzbc.p6.ui.window.focus_state


import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.common.focus_requester.P6FocusRequester
import org.mockito.Mockito.*
import kotlin.test.*

class SingleWindowFocusStateImpTest {
    lateinit var f : SingleWindowFocusStateImp
    lateinit var mockCursorFR:P6FocusRequester
    lateinit var mockEditorFR:P6FocusRequester
    @BeforeTest
    fun b(){
        mockCursorFR = mock(P6FocusRequester::class.java)
        mockEditorFR = mock(P6FocusRequester::class.java)
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
