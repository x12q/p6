package com.qxdzbc.p6.app.action.script

import com.qxdzbc.p6.app.action.script.new_script.rm.NewScriptRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding

import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=ScriptRM::class)
class ScriptRMImp @Inject constructor(

    private val ns: NewScriptRM,
) : ScriptRM, NewScriptRM by ns
