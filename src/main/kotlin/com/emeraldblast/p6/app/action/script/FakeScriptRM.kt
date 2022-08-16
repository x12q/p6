package com.emeraldblast.p6.app.action.script

import com.emeraldblast.p6.app.action.script.new_script.NewScriptRequest
import com.emeraldblast.p6.app.action.script.new_script.NewScriptResponse

class FakeScriptRM: ScriptRM{

    override fun newScript(request: NewScriptRequest): NewScriptResponse? {
        return null
    }

}
