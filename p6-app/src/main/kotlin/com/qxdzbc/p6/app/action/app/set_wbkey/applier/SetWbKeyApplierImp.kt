package com.qxdzbc.p6.app.action.app.set_wbkey.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.rpc.document.workbook.msg.SetWbKeyResponse
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfos.noNav
import javax.inject.Inject

class SetWbKeyApplierImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>,
    val errorRouter: ErrorRouter,
) : SetWbKeyApplier {
    private var stateCont by stateContMs
    private var scriptCont by stateCont.centralScriptContainerMs
    override fun applySetWbKey(res: RseNav<SetWbKeyResponse>): Rse<SetWbKeyResponse> {
        if (res is Ok) {
            val r = res.value
            val newWb = r.newWb
            val oldWbKey = r.oldWbKey
            // update wb cont
            if (newWb != null) {
                val newWbCont: WorkbookContainer = stateCont
                    .globalWbCont
                    .removeWb(oldWbKey)
                    .overwriteWB(newWb)
                stateCont.globalWbCont = newWbCont
            }
            // update wb state cont
            if (r.newWbStateCont != null) {
                stateCont.globalWbStateCont = r.newWbStateCont
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
