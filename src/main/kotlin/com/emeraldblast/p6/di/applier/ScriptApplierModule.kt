package com.emeraldblast.p6.di.applier

import com.emeraldblast.p6.app.action.script.ScriptApplier
import com.emeraldblast.p6.app.action.script.ScriptApplierImp
import com.emeraldblast.p6.app.action.script.new_script.applier.NewScriptApplier
import com.emeraldblast.p6.app.action.script.new_script.applier.NewScriptApplierImp
import com.emeraldblast.p6.app.action.script.script_change.applier.ScriptChangeApplier
import com.emeraldblast.p6.app.action.script.script_change.applier.ScriptChangeApplierImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface ScriptApplierModule {
    @Binds
    @P6Singleton
    fun ScriptChangeApplier(i: ScriptChangeApplierImp): ScriptChangeApplier
    @Binds
    @P6Singleton
    fun NewScriptApplier(i: NewScriptApplierImp): NewScriptApplier

    @Binds
    @P6Singleton
    fun ScriptApplier(i: ScriptApplierImp): ScriptApplier
}
