package com.qxdzbc.p6.app.action.app.close_wb.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.AppStateErrors
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.state.WindowState
import com.github.michaelbull.result.*
import javax.inject.Inject

class CloseWorkbookRMImp @Inject constructor(
    @StateContainerMs val stateContMs:Ms<StateContainer>
) : CloseWorkbookRM {
    private var stateCont by stateContMs
    private var globalWbStateCont by stateCont.globalWbStateContMs
    override fun closeWb(request: CloseWorkbookRequest): CloseWorkbookResponse? {
        val windowStateMs: Ms<WindowState>? = if (request.windowId != null) {
            stateCont.getWindowStateMsById(request.windowId)
        } else {
            stateCont.getWindowStateMsByWbKey(request.wbKey)
        }
        if (windowStateMs != null) {
            var windowState by windowStateMs
            val getWbRs = globalWbStateCont.getWbStateMsRs(request.wbKey)
            when (getWbRs) {
                is Ok -> {
                    return CloseWorkbookResponse(
                        isError = false,
                        errorReport = null,
                        wbKey = request.wbKey,
                        windowId = request.windowId
                    )
                }
                is Err -> {
                    return CloseWorkbookResponse(
                        isError = true,
                        errorReport = getWbRs.error,
                        wbKey = request.wbKey,
                        windowId = request.windowId
                    )
                }
            }
        } else {
            return CloseWorkbookResponse(
                isError = true,
                errorReport = AppStateErrors.InvalidWindowState.report1(request.wbKey),
                wbKey = request.wbKey,
                windowId = request.windowId
            )
        }
    }
}
