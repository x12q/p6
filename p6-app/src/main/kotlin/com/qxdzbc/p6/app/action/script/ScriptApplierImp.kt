package com.qxdzbc.p6.app.action.script

import com.qxdzbc.p6.app.action.script.new_script.applier.NewScriptApplier
import com.qxdzbc.p6.app.action.script.script_change.applier.ScriptChangeApplier
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=ScriptApplier::class)
class ScriptApplierImp @Inject constructor(
    val ns: NewScriptApplier,
    val sc: ScriptChangeApplier
) : ScriptApplier, NewScriptApplier by ns, ScriptChangeApplier by sc
