package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.global.GlobalAction
import com.qxdzbc.p6.app.action.global.GlobalActionImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface GlobalActionModule {
    @Binds
    @P6Singleton
    fun GlobalAction(i: GlobalActionImp):GlobalAction
}
