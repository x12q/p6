package com.qxdzbc.p6.app.action.app.close_wb.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.AppStateErrors
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseWorkbookRMImp @Inject constructor(
    val stateContMs:Ms<StateContainer>
) : CloseWorkbookRM {
    private var stateCont by stateContMs
    private var globalWbStateCont by stateCont.wbStateContMs
    override fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse {
        val windowStateMs: Ms<WindowState>? = if (request.windowId != null) {
            stateCont.getWindowStateMsById(request.windowId)
        } else {
            stateCont.getWindowStateMsByWbKey(request.wbKey)
        }
        if (windowStateMs != null) {
//            var windowState by windowStateMs
            val getWbRs = globalWbStateCont.getWbStateMsRs(request.wbKey)
            when (getWbRs) {
                is Ok -> {
                    return CloseWorkbookResponse(
                        errorReport = null,
                        wbKey = request.wbKey,
                        windowId = request.windowId
                    )
                }
                is Err -> {
                    return CloseWorkbookResponse(
                        errorReport = getWbRs.error,
                        wbKey = request.wbKey,
                        windowId = request.windowId
                    )
                }
            }
        } else {
            return CloseWorkbookResponse(
                errorReport = AppStateErrors.InvalidWindowState.report1(request.wbKey),
                wbKey = request.wbKey,
                windowId = request.windowId
            )
        }
    }
}
