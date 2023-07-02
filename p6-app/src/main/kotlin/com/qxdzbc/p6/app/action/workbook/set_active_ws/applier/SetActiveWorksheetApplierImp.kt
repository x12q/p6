package com.qxdzbc.p6.app.action.workbook.set_active_ws.applier

import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.app.common.utils.RseNav

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWorksheetApplierImp @Inject constructor(
    private val errorRouter: ErrorRouter,
    private val subAppStateContainer: StateContainer,
) : SetActiveWorksheetApplier {

    override fun apply(res: RseNav<SetActiveWorksheetResponse2>): RseNav<SetActiveWorksheetResponse2> {
        res.onSuccess {rs:SetActiveWorksheetResponse2->
            val k = rs.request.wbKey

            rs.newActiveWbPointer?.also {
                val wdStateMs = subAppStateContainer.getWindowStateMsByWbKey(k)
                if(wdStateMs!=null){
                    wdStateMs.value.activeWbPointerMs.value = it
                }
            }

            rs.newWbState?.also {
                val wbStateMs = subAppStateContainer.getWbStateMs(k)
                if(wbStateMs!=null){
                    wbStateMs.value = it
                }
            }



        }.onFailure {
            errorRouter.publish(it)
        }
        return res
    }
}
