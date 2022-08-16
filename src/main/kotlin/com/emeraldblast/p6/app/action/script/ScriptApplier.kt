package com.emeraldblast.p6.app.action.script

import com.emeraldblast.p6.app.action.script.new_script.applier.NewScriptApplier
import com.emeraldblast.p6.app.action.script.script_change.applier.ScriptChangeApplier

interface ScriptApplier: NewScriptApplier, ScriptChangeApplier {
}
