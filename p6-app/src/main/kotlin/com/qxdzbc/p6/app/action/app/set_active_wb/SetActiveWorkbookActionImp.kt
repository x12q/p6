package com.qxdzbc.p6.app.action.app.set_active_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.AppStateErrors
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import javax.inject.Inject

class SetActiveWorkbookActionImp @Inject constructor(
    @StateContainerSt
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val appStateMs: Ms<AppState>,
) : SetActiveWorkbookAction {

    private var appState: AppState by appStateMs
    val stateCont by stateContSt

    override fun setActiveWb(wbk: WorkbookKey): Rse<Unit> {
        val windowStateRs = stateCont.getWindowStateByWbKeyRs(wbk)
        val rt= windowStateRs.map {owds->
            val wds = owds
            appState.activeWindowPointer = appState.activeWindowPointer.pointTo(wds.id)
            stateCont.getWbKeyMs(wbk)?.also {
                wds.activeWbPointer = wds.activeWbPointer.pointTo(it)
            }
            Unit
        }
        return rt
    }
}
