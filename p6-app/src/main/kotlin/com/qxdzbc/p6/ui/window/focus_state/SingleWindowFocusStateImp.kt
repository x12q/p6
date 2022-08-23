package com.qxdzbc.p6.ui.window.focus_state

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.di.FalseMs
import com.qxdzbc.p6.di.TrueMs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState
import javax.inject.Inject

/**
 * A focus state that can only focus on 1 or 0 thing at a time
 */
data class SingleWindowFocusStateImp @Inject constructor(
    @TrueMs
    private val isCursorFocusedMs:Ms<Boolean>,
    @FalseMs
    private val isEditorFocusedMs:Ms<Boolean>,
) : WindowFocusState {

    override val isCursorFocused: Boolean by isCursorFocusedMs
    private val l = listOf(isCursorFocusedMs, isEditorFocusedMs)

    override fun freeAllFocus(): WindowFocusState {
        l.forEach {
            it.value = false
        }
        return this
    }

    override fun restoreDefault(): WindowFocusState {
        return this.focusOnCursor()
    }

    override fun focusOnCursor(): WindowFocusState {
        freeAllFocus()
        isCursorFocusedMs.value = true
        return this
    }

    override fun freeFocusOnCursor(): WindowFocusState {
        isCursorFocusedMs.value = false
        return this
    }

    override val isEditorFocused: Boolean by isEditorFocusedMs

    override fun focusOnEditor(): WindowFocusState {
        freeAllFocus()
        isEditorFocusedMs.value = true
        return this
    }

    override fun freeFocusOnEditor(): WindowFocusState {
        isEditorFocusedMs.value = false
        return this
    }
}
