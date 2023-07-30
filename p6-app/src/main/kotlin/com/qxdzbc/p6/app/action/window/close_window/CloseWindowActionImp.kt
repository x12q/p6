package com.qxdzbc.p6.app.action.window.close_window

import androidx.compose.ui.window.ApplicationScope
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer

import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWindowActionImp @Inject constructor(
    private val appScope: ApplicationScope?,
    private val subAppStateContainer: StateContainer,
) : CloseWindowAction {

    private var stateCont = subAppStateContainer

    override fun closeWindow(windowId: String):Rse<Unit> {
        val rs = stateCont.getWindowStateByIdRs(windowId = windowId).map{windowState->
            if (stateCont.windowStateMsList.size == 1) {
                appScope?.exitApplication()
            } else {
                stateCont.removeWindowState(windowId)
            }
            Unit
        }
        return rs
    }

    override fun closeWindow(withWindowId: WithWindowId): Rse<Unit> {
        return this.closeWindow(withWindowId.windowId)
    }
}
