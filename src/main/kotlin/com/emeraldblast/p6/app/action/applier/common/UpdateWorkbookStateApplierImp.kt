package com.emeraldblast.p6.app.action.applier.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import javax.inject.Inject

class UpdateWorkbookStateApplierImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>,
) : UpdateWorkbookStateApplier {
    private var appState by appStateMs
    override fun updateWbState(newWbState: WorkbookState?) {
        if(newWbState!=null){
            appState.globalWbStateCont = appState.globalWbStateCont.updateWbState(newWbState)
        }
    }
}
