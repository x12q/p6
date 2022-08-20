package com.emeraldblast.p6.app.action.applier.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.di.state.app_state.StateContainerMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.StateContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class UpdateWbApplierImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>,
) : UpdateWbApplier {
    private var stateCont by stateContMs
    override fun updateWb(newWb: Workbook?) {
        if (newWb != null) {
            stateCont.globalWbCont = stateCont.globalWbCont.overwriteWB(newWb)
        }
    }
}
