package com.emeraldblast.p6.di.request_maker

import com.emeraldblast.p6.app.action.script.ScriptRM
import com.emeraldblast.p6.app.action.script.ScriptRMImp
import com.emeraldblast.p6.app.action.script.new_script.rm.FakeNewScriptRM
import com.emeraldblast.p6.app.action.script.new_script.rm.NewScriptRMImp
import com.emeraldblast.p6.di.Fake
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds
import com.emeraldblast.p6.app.action.script.new_script.rm.NewScriptRM as NewScriptRM1

@dagger.Module
interface ScriptRMModule {
    @Binds
    @P6Singleton
    
    fun NewScriptRMLocal(i: NewScriptRMImp): NewScriptRM1

    @Binds
    @P6Singleton
    @com.emeraldblast.p6.di.Fake
    fun FakeNewScriptRM(i: FakeNewScriptRM): NewScriptRM1

    @Binds
    @P6Singleton
    fun ScriptRequestMaker(i: ScriptRMImp): ScriptRM

}
