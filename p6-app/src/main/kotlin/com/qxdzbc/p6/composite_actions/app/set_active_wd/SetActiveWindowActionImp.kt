package com.qxdzbc.p6.composite_actions.app.set_active_wd

import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWindowActionImp @Inject constructor(
    val activeWdPointer: ActiveWindowPointer,
    val stateCont:StateContainer
) : SetActiveWindowAction {
    val sc  = stateCont
    var wdp = activeWdPointer
    override fun setActiveWindow(windowId: String): Rse<Unit> {
        val wdStateRs = sc.getWindowStateByIdRs(windowId)
        val rt = wdStateRs
            .onSuccess {
                wdp.pointTo(windowId)
            }.map { Unit }
        return rt
    }
}
