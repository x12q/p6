package com.qxdzbc.p6.ui.window.di.comp

import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.qxdzbc.p6.ui.window.state.WindowStateFactory.Companion.createDefault
import dagger.Module
import dagger.Provides

@Module
interface WindowModule{
    companion object{
        @Provides
        @WindowScope
        fun windowState(windowStateFactory: WindowStateFactory):WindowState{
            return windowStateFactory.createDefault()
        }
    }
}