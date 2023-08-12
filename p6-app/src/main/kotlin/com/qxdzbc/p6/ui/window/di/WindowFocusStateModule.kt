package com.qxdzbc.p6.ui.window.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.di.qualifiers.DefaultFocusStateMs
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import dagger.Module
import dagger.Provides

@Module
interface WindowFocusStateModule {
    companion object{
        @Provides
        @DefaultFocusStateMs
        fun FocusStateMs(i: WindowFocusState):Ms<WindowFocusState>{
            return ms(i)
        }
    }
}
