package com.emeraldblast.p6.app.action.app.set_wbkey.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyRequest
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyResponse
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.*
import javax.inject.Inject

class SetWbKeyRMImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>
) : SetWbKeyRM {
    private var appState by appStateMs

    override fun setWbKeyRequest(req: SetWbKeyRequest): RseNav<SetWbKeyResponse> {
        val oldKey = req.wbKey
        val newKey = req.newWbKey
        val z = appState.getWorkbookRs(oldKey).flatMap { oldWb->
            val newWb = oldWb.setKey(newKey)
            val newWbState = appState
                .getWbStateMs(oldKey)
                ?.value
                ?.setWorkbookKey(newKey)
            val newWbStateCont = appState.globalWbStateCont.replaceKey(oldKey,newKey)

            val windowStateMs = appState.getWindowStateMsByWbKey(oldKey)
            val newWindowState = windowStateMs?.value?.replaceWorkbookKey(oldKey,newKey)
            val newCentralScriptCont = appState.centralScriptContainer.replaceWbKey(oldKey,newKey)
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
