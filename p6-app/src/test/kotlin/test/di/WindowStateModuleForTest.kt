package test.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.common.focus_requester.FocusRequesterWrapper
import com.qxdzbc.p6.di.state.window.DefaultFocusStateMs
import com.qxdzbc.p6.ui.window.focus_state.SingleWindowFocusStateImp
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import dagger.Provides

@dagger.Module
interface WindowStateModuleForTest {
    companion object {
        @Provides
        @DefaultFocusStateMs
        fun FocusStateMs(): Ms<WindowFocusState> {
            return ms(
                SingleWindowFocusStateImp(
                    isCursorFocusedMs = ms(true),
                    isEditorFocusedMs = ms(false),
                    cursorFocusRequester = FocusRequesterWrapper.fake,
                    editorFocusRequester = FocusRequesterWrapper.fake,
                )
            )
        }
    }
}

