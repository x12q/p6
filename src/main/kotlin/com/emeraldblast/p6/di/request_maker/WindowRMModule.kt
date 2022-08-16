package com.emeraldblast.p6.di.request_maker

import com.emeraldblast.p6.app.action.window.WindowRM
import com.emeraldblast.p6.app.action.window.WindowRMImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface WindowRMModule {
    @Binds
    @P6Singleton
    fun WindowRequestMaker(i: WindowRMImp): WindowRM
}
