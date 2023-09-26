package com.qxdzbc.p6.composite_actions.window.close_wb_ui

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.workbook.state.WorkbookState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class CheckNeedSaveUIActionImp @Inject constructor(
    val stateContainerSt:StateContainer
): CheckNeedSaveUIAction {

    val sc  = stateContainerSt

    override fun checkNeedSave(wbKeySt: St<WorkbookKey>): Boolean {
        val rt = sc.getWbState(wbKeySt)?.let{
            checkNeedSave(it)
        } ?: false
        return rt
    }

    fun checkNeedSave(wbState:WorkbookState):Boolean{
        if(wbState.needSave){

        }
        return wbState.needSave
    }

    override fun checkNeedSave(wbKey: WorkbookKey): Boolean {
        val rt = sc.getWbState(wbKey)?.let{
            checkNeedSave(it)
        } ?: false
        return rt
    }
}
