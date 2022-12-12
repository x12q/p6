package com.qxdzbc.p6.app.action.app.restart_kernel.applier


import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.restart_kernel.RestartKernelResponse
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(P6AnvilScope::class)
class RestartKernelApplierImp @Inject constructor(
    private val appStateMs: Ms<AppState>,
    val stateContMs:Ms<StateContainer>,
) : RestartKernelApplier {
    private var stateCont by stateContMs
    private var appState by appStateMs
    override fun applyRestartKernel(response: RestartKernelResponse) {
        if (response.isError) {
            val oddContMs = if (response.windowId != null) {
                val odMs = stateCont.getWindowStateMsById(response.windowId)?.value?.errorContainerMs
                odMs
            } else {
                appState.errorContainerMs
            }

            if (oddContMs != null) {
                oddContMs.value = oddContMs.value.addErrorReport(response.errorReport)
            }
        }
    }
}
