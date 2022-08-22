package com.qxdzbc.p6.app.action.script.new_script.applier

import com.qxdzbc.p6.app.action.script.new_script.NewScriptNotification
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse

interface NewScriptApplier {
    fun applyNewScript(res: NewScriptResponse?)
    fun applyNewScriptNotif(notif: NewScriptNotification)
}
