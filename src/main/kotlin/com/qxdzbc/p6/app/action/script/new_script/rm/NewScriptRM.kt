package com.qxdzbc.p6.app.action.script.new_script.rm

import com.qxdzbc.p6.app.action.script.new_script.NewScriptRequest
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse

interface NewScriptRM {
    fun newScript(request: NewScriptRequest): NewScriptResponse?
}
