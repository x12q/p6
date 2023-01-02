package com.qxdzbc.p6.app.action.app.set_active_wb


import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWorkbookActionImp @Inject constructor(
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
                wds.activeWbPointerMs.value = wds.activeWbPointer.pointTo(it)
            }
            Unit
        }
        return rt
    }
}
