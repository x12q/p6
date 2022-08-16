package com.emeraldblast.p6.app.action.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class WorkbookUpdateCommonApplierImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val baseApplier: BaseApplier
) : WorkbookUpdateCommonApplier {

    private var appState by appStateMs
    private var wbCont by appState.globalWbContMs
    private var globalWbStateCont by appState.globalWbStateContMs

    override fun apply(res: WorkbookUpdateCommonResponseInterface?) {
        baseApplier.applyRes(res){
            val wbKey = it.wbKey
            if (wbKey != null) {
                appState.getWindowStateMsByWbKey(wbKey)?.also { windowStateMs ->
                    val newColdWb = it.newWorkbook
                    if (newColdWb != null) {
                        val computedBook = newColdWb.reRun()
                        wbCont = wbCont.overwriteWB(computedBook)
                        globalWbStateCont.getWbStateMs(wbKey)?.also {
                            val wbStateMs = it
                            wbStateMs.value =
                                wbStateMs.value.setWorkbookKeyAndRefreshState(newColdWb.key).setNeedSave(true)
                        }
                    }
                }
            }
        }
    }
}
