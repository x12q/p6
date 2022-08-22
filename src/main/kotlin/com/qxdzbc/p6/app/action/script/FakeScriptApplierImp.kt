package com.qxdzbc.p6.app.action.script

import com.qxdzbc.p6.app.action.script.new_script.NewScriptNotification
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse
import com.qxdzbc.p6.app.action.script.script_change.ScriptChangeResponse

class FakeScriptApplierImp : ScriptApplier {
    override fun applyNewScript(res: NewScriptResponse?) {
        println("applyNewScript")
    }

    override fun applyNewScriptNotif(notif: NewScriptNotification) {
        TODO("Not yet implemented")
    }

    override fun applyScriptChange(res: ScriptChangeResponse?) {
        println("applyScriptChange")
    }
}
