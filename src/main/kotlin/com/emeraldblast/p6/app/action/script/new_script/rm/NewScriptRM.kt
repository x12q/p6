package com.emeraldblast.p6.app.action.script.new_script.rm

import com.emeraldblast.p6.app.action.script.new_script.NewScriptRequest
import com.emeraldblast.p6.app.action.script.new_script.NewScriptResponse

interface NewScriptRM {
    fun newScript(request: NewScriptRequest): NewScriptResponse?
}
