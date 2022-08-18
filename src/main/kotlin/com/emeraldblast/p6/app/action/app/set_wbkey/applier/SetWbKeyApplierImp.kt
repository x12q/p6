package com.emeraldblast.p6.app.action.app.set_wbkey.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.common.utils.ErrorUtils.noNav
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyResponse
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.*
import javax.inject.Inject

class SetWbKeyApplierImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>,
    val errorRouter: ErrorRouter,
) : SetWbKeyApplier {
    private var appState by appStateMs
    private var scriptCont by appState.centralScriptContainerMs
    override fun applySetWbKey(res: RseNav<SetWbKeyResponse>): Rse<SetWbKeyResponse> {
        if (res is Ok) {
            val r = res.value
            val newWb = r.newWb
            val oldWbKey = r.oldWbKey
            // update wb cont
            if (newWb != null) {
                val newWbCont: WorkbookContainer = appState
                    .globalWbCont
                    .removeWb(oldWbKey)
                    .overwriteWB(newWb)
                appState.globalWbCont = newWbCont
            }
            // update wb state cont
            if (r.newWbStateCont != null) {
                appState.globalWbStateCont = r.newWbStateCont
            }

            // update states
            if (r.windowStateMs != null && r.newWindowState != null) {
                r.windowStateMs.value = r.newWindowState
            }
            // update script cont
            if(r.newCentralScriptCont!=null){
                scriptCont = r.newCentralScriptCont
            }
            return res
        } else {
            errorRouter.publish(res.component2()!!)
            return res.noNav()
        }
    }
}
