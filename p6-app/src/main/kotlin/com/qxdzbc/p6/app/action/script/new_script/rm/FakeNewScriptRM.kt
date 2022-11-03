package com.qxdzbc.p6.app.action.script.new_script.rm

import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.script.new_script.NewScriptRequest
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse
import com.qxdzbc.p6.di.Fake
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
@Fake
class FakeNewScriptRM @Inject constructor(
) : NewScriptRM {
    override fun newScript(request: NewScriptRequest): NewScriptResponse? {
        return NewScriptResponse(SingleSignalResponse(null))
    }
}
