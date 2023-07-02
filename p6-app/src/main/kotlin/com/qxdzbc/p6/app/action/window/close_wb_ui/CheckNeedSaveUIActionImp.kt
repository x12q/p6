package com.qxdzbc.p6.app.action.window.close_wb_ui

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
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
