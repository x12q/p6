package com.emeraldblast.p6.di.state.window

import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.ms
import com.emeraldblast.p6.ui.window.focus_state.WindowFocusState
import com.emeraldblast.p6.ui.window.focus_state.SingleWindowFocusStateImp
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
