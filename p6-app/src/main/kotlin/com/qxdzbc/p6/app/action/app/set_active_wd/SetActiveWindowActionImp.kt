package com.qxdzbc.p6.app.action.app.set_active_wd

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWindowActionImp @Inject constructor(
    val activeWdPointerMs: Ms<ActiveWindowPointer>,
    val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : SetActiveWindowAction {
    val sc by stateContSt
    var wdp by activeWdPointerMs
    override fun setActiveWindow(windowId: String): Rse<Unit> {
        val wdStateRs = sc.getWindowStateByIdRs(windowId)
        val rt = wdStateRs
            .onSuccess {
                wdp = wdp.pointTo(windowId)
            }.map { Unit }
        return rt
    }
}
