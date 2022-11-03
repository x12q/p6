package com.qxdzbc.p6.app.action.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import javax.inject.Inject

class WorkbookUpdateCommonApplierImp @Inject constructor(
    val stateContMs:Ms<StateContainer>,
    private val baseApplier: BaseApplier
) : WorkbookUpdateCommonApplier {
    private var stateCont by stateContMs

    private var wbCont by stateCont.wbContMs
    private var globalWbStateCont by stateCont.wbStateContMs

    override fun apply(res: WorkbookUpdateCommonResponseInterface?) {
        baseApplier.applyRes(res){
            val wbKey = it.wbKey
            if (wbKey != null) {
                stateCont.getWindowStateMsByWbKey(wbKey)?.also { windowStateMs ->
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
