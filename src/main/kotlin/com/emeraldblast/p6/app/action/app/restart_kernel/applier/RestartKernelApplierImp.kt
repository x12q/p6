package com.emeraldblast.p6.app.action.app.restart_kernel.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.app.restart_kernel.RestartKernelResponse
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.di.state.app_state.StateContainerMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.StateContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class RestartKernelApplierImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    @StateContainerMs val stateContMs:Ms<StateContainer>,
) : RestartKernelApplier {
    private var stateCont by stateContMs
    private var appState by appStateMs
    override fun applyRestartKernel(response: RestartKernelResponse) {
        if (response.isError) {
            val oddContMs = if (response.windowId != null) {
                val odMs = stateCont.getWindowStateMsById(response.windowId)?.value?.oddityContainerMs
                odMs
            } else {
                appState.oddityContainerMs
            }

            if (oddContMs != null) {
                oddContMs.value = oddContMs.value.addErrorReport(response.errorReport)
            }
        }
    }
}
