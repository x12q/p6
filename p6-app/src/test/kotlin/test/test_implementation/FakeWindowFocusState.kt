package test.test_implementation

import com.qxdzbc.p6.common.focus_requester.P6FocusRequester
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState

data class FakeWindowFocusState(
    override val isCursorFocused: Boolean = true,
    override val isEditorFocused: Boolean = false,
) : WindowFocusState {
    override fun freeAllFocus(): WindowFocusState {
        return this.copy(
            isCursorFocused = false,
            isEditorFocused = false
        )
    }

    override fun restoreDefault(): WindowFocusState {
        return this.copy(
            isCursorFocused = true, isEditorFocused = false
        )
    }

    override fun setCursorFocus(i: Boolean): WindowFocusState {
        return this.copy(isCursorFocused=i)
    }

    override fun focusOnCursor(): WindowFocusState {
        return this.copy(isCursorFocused = true,isEditorFocused = false)
    }

    override fun freeFocusOnCursor(): WindowFocusState {
        return setCursorFocus(false)
    }

    override fun setCellEditorFocus(i: Boolean): WindowFocusState {
        return this.copy(isEditorFocused=true)
    }

    override fun focusOnEditor(): WindowFocusState {
        return this.copy(isCursorFocused = false,isEditorFocused = true)
    }

    override fun freeFocusOnEditor(): WindowFocusState {
        return setCellEditorFocus(false)
    }

    override val cursorFocusRequester: com.qxdzbc.p6.common.focus_requester.P6FocusRequester = com.qxdzbc.p6.common.focus_requester.P6FocusRequester.fake
    override val editorFocusRequester: com.qxdzbc.p6.common.focus_requester.P6FocusRequester = com.qxdzbc.p6.common.focus_requester.P6FocusRequester.fake
}
