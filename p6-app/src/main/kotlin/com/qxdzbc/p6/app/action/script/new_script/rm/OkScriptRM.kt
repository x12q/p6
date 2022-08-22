package com.qxdzbc.p6.app.action.script.new_script.rm

import com.qxdzbc.p6.app.action.script.new_script.NewScriptRequest
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse
import javax.inject.Inject

/**
 * do nothing, just return an OK response
 * for testing
 */
class OkScriptRM @Inject constructor() : NewScriptRM {
    override fun newScript(request: NewScriptRequest): NewScriptResponse? {
        return NewScriptResponse.fromErrReport(null)
    }
}
