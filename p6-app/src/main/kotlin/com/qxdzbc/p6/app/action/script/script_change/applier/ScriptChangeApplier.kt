package com.qxdzbc.p6.app.action.script.script_change.applier

import com.qxdzbc.p6.app.action.script.script_change.ScriptChangeResponse

interface ScriptChangeApplier{
    fun applyScriptChange(res: ScriptChangeResponse?)
}
