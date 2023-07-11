package com.qxdzbc.p6.app.action.worksheet.delete_multi.applier

import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellResponse
import com.qxdzbc.p6.app.common.utils.RseNav

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteMultiApplierImp @Inject constructor(
    private val stateCont: StateContainer,
    private val errorRouter: ErrorRouter,
    private val wbCont: WorkbookContainer,
) : DeleteMultiApplier {

    override fun apply(res: RseNav<RemoveMultiCellResponse>): RseNav<RemoveMultiCellResponse> {
        res
            .onFailure { err ->
                errorRouter.publish(err)
            }
            .onSuccess { r ->
                val k = r.newWb.key
                // x: remove cell from ws, wb
                wbCont.overwriteWB(r.newWb)
                // x: remove cell state from ws state
                r.newWsState?.also { wss ->
//                    r.newWb.addSheetOrOverwrite(wss.worksheet)
//                    val wsms = stateCont.getWsStateMs(k, wss.name)
//                    if (wsms != null) {
//                        wsms.value = wss
//                    }
                }
            }
        return res
    }
}
