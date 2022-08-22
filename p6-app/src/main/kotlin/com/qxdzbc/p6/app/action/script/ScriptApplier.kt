package com.qxdzbc.p6.app.action.script

import com.qxdzbc.p6.app.action.script.new_script.applier.NewScriptApplier
import com.qxdzbc.p6.app.action.script.script_change.applier.ScriptChangeApplier

interface ScriptApplier: NewScriptApplier, ScriptChangeApplier {
}
