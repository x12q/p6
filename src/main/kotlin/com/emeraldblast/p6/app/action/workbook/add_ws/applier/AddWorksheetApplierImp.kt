package com.emeraldblast.p6.app.action.workbook.add_ws.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.di.state.app_state.StateContainerMs
import com.emeraldblast.p6.rpc.document.workbook.msg.AddWorksheetResponse
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.StateContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import javax.inject.Inject

class AddWorksheetApplierImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>,
    @StateContainerMs private val stateContMs:Ms<StateContainer>
) : AddWorksheetApplier {
    private var stateCont by stateContMs
    private var appState by appStateMs

    override fun applyAddWs(res: RseNav<AddWorksheetResponse>): RseNav<AddWorksheetResponse> {
        val rt = res.andThen { addRs ->
            appState.globalWbCont = appState.globalWbCont.overwriteWB(addRs.newWb)
            stateCont = stateCont.addWbStateFor(addRs.newWb)
            val wbMs=stateCont.getWbStateMs(addRs.newWb.key)
            if(wbMs!=null){
                wbMs.value = wbMs.value.refreshWsState()
            }
            Ok(addRs)
        }
        return rt
    }
}
