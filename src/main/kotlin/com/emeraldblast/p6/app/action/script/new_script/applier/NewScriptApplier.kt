package com.emeraldblast.p6.app.action.script.new_script.applier

import com.emeraldblast.p6.app.action.script.new_script.NewScriptNotification
import com.emeraldblast.p6.app.action.script.new_script.NewScriptResponse

interface NewScriptApplier {
    fun applyNewScript(res: NewScriptResponse?)
    fun applyNewScriptNotif(notif: NewScriptNotification)
}
