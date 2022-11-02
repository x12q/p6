package com.qxdzbc.p6.di.request_maker

import com.qxdzbc.p6.app.action.window.WindowRM
import com.qxdzbc.p6.app.action.window.WindowRMImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface WindowRMModule {
    @Binds
    @P6Singleton
    fun WindowRM(i: WindowRMImp): WindowRM
}
