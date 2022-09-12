package com.qxdzbc.p6.app.action.worksheet.delete_multi.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiResponse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject

class DeleteMultiApplierImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>,
    private val errorRouter: ErrorRouter,
) : DeleteMultiApplier {
    private var appState by appStateMs
    override fun apply(res: RseNav<DeleteMultiResponse>): RseNav<DeleteMultiResponse> {
        res.onFailure {
            errorRouter.publish(it)
        }.onSuccess {r->
            val k = r.newWb.key
            // x: remove cell from ws, wb
            appState.wbCont = appState.wbCont.overwriteWB(r.newWb)
            // x: remove cell state from ws state
            r.newWsState?.also {wss->
               val wsms=appState.getWsStateMs(k,wss.name)
                if(wsms!=null){
                    wsms.value = wss
                }
            }

        }
        return res
    }
}
