package com.qxdzbc.p6.app.action.script.script_change.applier

import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.script.script_change.ScriptChangeResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ScriptChangeApplierImp @Inject constructor(
    private val baseApplier: BaseApplier
) : ScriptChangeApplier {
    override fun applyScriptChange(res: ScriptChangeResponse?) {
        baseApplier.applyRes(res) {
            // do nothing
        }
    }
}
