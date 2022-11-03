package com.qxdzbc.p6.app.action.window.close_window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ApplicationScope
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWindowActionImp @Inject constructor(
    private val appScope: ApplicationScope?,
    private val stateContMs:Ms<SubAppStateContainer>,
) : CloseWindowAction {

    private var stateCont by stateContMs

    override fun closeWindow(windowId: String):Rse<Unit> {
        val rs = stateCont.getWindowStateByIdRs(windowId = windowId).map{windowStateMs->
            if (stateCont.windowStateMsList.size == 1) {
                appScope?.exitApplication()
            } else {
                stateCont = stateCont.removeWindowState(windowId)
            }
            Unit
        }
        return rs
    }

    override fun closeWindow(withWindowId: WithWindowId): Rse<Unit> {
        return this.closeWindow(withWindowId.windowId)
    }
}
