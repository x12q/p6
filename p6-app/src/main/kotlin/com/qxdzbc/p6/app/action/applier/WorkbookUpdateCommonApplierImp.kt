package com.qxdzbc.p6.app.action.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class WorkbookUpdateCommonApplierImp @Inject constructor(
    val stateContMs:Ms<StateContainer>,
    private val errorRouter: ErrorRouter,
) : WorkbookUpdateCommonApplier {
    private var stateCont by stateContMs

    private var wbCont by stateCont.wbContMs
    private var globalWbStateCont by stateCont.wbStateContMs

    override fun apply(res: WorkbookUpdateCommonResponseInterface?) {
        if(res!=null){
            val errR=res.errorReport
            if(errR!=null){
                errorRouter.publishToWindow(errR,res.windowId,res.wbKey)
            }else{
                val wbKey = res.wbKey
                if (wbKey != null) {
                    stateCont.getWindowStateMsByWbKey(wbKey)?.also { windowStateMs ->
                        val newColdWb = res.newWorkbook
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
}
