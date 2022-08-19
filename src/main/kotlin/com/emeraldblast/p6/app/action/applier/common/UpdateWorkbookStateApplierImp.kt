package com.emeraldblast.p6.app.action.applier.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.di.state.app_state.StateContainerMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.StateContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import javax.inject.Inject

class UpdateWorkbookStateApplierImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>,
) : UpdateWorkbookStateApplier {
    private var stateCont by stateContMs
    override fun updateWbState(newWbState: WorkbookState?) {
        if(newWbState!=null){
            stateCont.globalWbStateCont = stateCont.globalWbStateCont.updateWbState(newWbState)
        }
    }
}
