package com.emeraldblast.p6.app.action.applier.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class UpdateWbApplierImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>,
) : UpdateWbApplier {
    private var appState by appStateMs
    override fun updateWb(newWb: Workbook?) {
        if (newWb != null) {
            appState.globalWbCont = appState.globalWbCont.overwriteWB(newWb)
        }
    }
}
