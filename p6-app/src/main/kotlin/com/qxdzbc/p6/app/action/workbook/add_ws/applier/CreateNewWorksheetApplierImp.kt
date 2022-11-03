package com.qxdzbc.p6.app.action.workbook.add_ws.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.utils.RseNav


import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import javax.inject.Inject

class CreateNewWorksheetApplierImp @Inject constructor(
    private val appStateMs: Ms<AppState>,
    private val stateContMs:Ms<SubAppStateContainer>
) : CreateNewWorksheetApplier {
    private var stateCont by stateContMs
    private var appState by appStateMs

    override fun applyRs(res: RseNav<AddWorksheetResponse>): RseNav<AddWorksheetResponse> {
        val rt = res.andThen { addRs ->
            appState.wbCont = appState.wbCont.overwriteWB(addRs.newWb)
            val wbMs=stateCont.getWbStateMs(addRs.newWb.key)
            if(wbMs!=null){
                wbMs.value = wbMs.value.refreshWsState()
            }
            Ok(addRs)
        }
        return rt
    }
}
