package com.qxdzbc.p6.app.action.script.script_change.applier

import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.script.script_change.ScriptChangeResponse
import javax.inject.Inject

class ScriptChangeApplierImp @Inject constructor(
    private val baseApplier: BaseApplier
) : ScriptChangeApplier {
    override fun applyScriptChange(res: ScriptChangeResponse?) {
        baseApplier.applyRes(res) {
            // do nothing
        }
    }
}