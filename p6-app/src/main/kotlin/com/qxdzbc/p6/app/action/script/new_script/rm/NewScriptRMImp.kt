package com.qxdzbc.p6.app.action.script.new_script.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.script.new_script.NewScriptRequest
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class NewScriptRMImp @Inject constructor(
    val appStateMs: Ms<AppState>
) : NewScriptRM {

    private var appState by appStateMs

    override fun newScript(request: NewScriptRequest): NewScriptResponse? {
        val se = request.scriptEntry
        val rs = appState.centralScriptContainer.addScriptRs(request.scriptEntry)
        when(rs){
            is Ok ->{
                appState.centralScriptContainer = rs.value
                return NewScriptResponse(
                    SingleSignalResponse(null)
                )
            }
            is Err ->{
                return NewScriptResponse(
                    SingleSignalResponse(rs.error)
                )
            }
        }
    }
}
