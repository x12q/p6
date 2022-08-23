package com.qxdzbc.p6.di.state.window

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.focus_state.SingleWindowFocusStateImp
import dagger.Binds
import dagger.Provides

@dagger.Module
interface WindowStateModule {
    @Binds
    fun FocusState(i: SingleWindowFocusStateImp):WindowFocusState
    companion object{
        @Provides
        @FocusStateMs
        fun FocusStateMs(i:WindowFocusState):Ms<WindowFocusState>{
            return ms(i)
        }
    }
}
