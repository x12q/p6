package com.qxdzbc.p6.ui.window.focus_state


import com.qxdzbc.p6.ui.common.compose.StateUtils.toMs
import kotlin.test.*

class SingleWindowFocusStateImpTest {
    lateinit var f : SingleWindowFocusStateImp
    @BeforeTest
    fun b(){
        f = SingleWindowFocusStateImp(
            isCursorFocusedMs = false.toMs(),
            isEditorFocusedMs = false.toMs(),
        )
    }
    @Test
    fun focusOnCursor() {
        val f2 = f.focusOnCursor()
        assertTrue(f2.isCursorFocused)
        assertFalse(f2.isEditorFocused)
    }

    @Test
    fun freeFocusOnCursor() {
        val f2 = f.focusOnCursor()
        assertTrue(f2.isCursorFocused)
        assertFalse(f2.freeFocusOnCursor().isCursorFocused)
    }

    @Test
    fun focusOnEditor() {
        val f2 = f.focusOnEditor()
        assertTrue(f2.isEditorFocused)
        assertFalse(f2.isCursorFocused)
    }

    @Test
    fun freeFocusOnEditor() {
        val f2 = f.focusOnEditor()
        assertTrue(f2.isEditorFocused)
        assertFalse(f2.freeFocusOnEditor().isCursorFocused)
    }
}
