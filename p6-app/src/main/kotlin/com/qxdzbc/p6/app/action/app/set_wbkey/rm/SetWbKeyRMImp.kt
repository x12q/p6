package com.qxdzbc.p6.app.action.app.set_wbkey.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyRequest
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyResponse
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import javax.inject.Inject

class SetWbKeyRMImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>,
) : SetWbKeyRM {

    private var stateCont by stateContMs

    override fun setWbKeyRequest(req: SetWbKeyRequest): RseNav<SetWbKeyResponse> {
        val oldKey = req.wbKey
        val newKey = req.newWbKey
        val z = stateCont.getWbRs(oldKey).flatMap { oldWb->
            val newWb = oldWb.setKey(newKey)
            val newWbState = stateCont
                .getWbStateMs(oldKey)
                ?.value
                ?.setWorkbookKey(newKey)
            val newWbStateCont = stateCont.wbStateCont.replaceKey(oldKey,newKey)

            val windowStateMs = stateCont.getWindowStateMsByWbKey(oldKey)
            val newWindowState = windowStateMs?.value?.replaceWorkbookKey(oldKey,newKey)
            val newCentralScriptCont = stateCont.centralScriptContainer.replaceWbKey(oldKey,newKey)
            val rt = SetWbKeyResponse(
                oldWbKey = oldKey,
                newWb= newWb,
                newWbState = newWbState,
                newWbStateCont = newWbStateCont,
                windowStateMs = windowStateMs,
                newWindowState = newWindowState,
                newCentralScriptCont = newCentralScriptCont
            )
            Ok(rt)
        }
        return z.mapError { it.withNav(req) }
    }
}
