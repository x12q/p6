package com.emeraldblast.p6.app.action.workbook.set_active_ws.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetWithIndexRequest
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.mapError
import javax.inject.Inject

class SetActiveWorksheetRMImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>
) : SetActiveWorksheetRM {

    private var appState by appStateMs

    override fun setActiveWs(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2> {
        val wbk = request.wbKey
        val wbStateMs = appState
            .getWbStateMs(wbk)?.value
            ?.setActiveSheet(request.wsName)
        val wdState = appState.getWindowStateMsByWbKey(wbk)
        val newWindowPointer = appState
            .activeWindowPointer
            .pointTo(wdState?.value?.id)
        val newActiveWbPointer = wdState?.value
            ?.activeWorkbookPointer
            ?.pointTo(wbk)
        val rt = Ok(
            SetActiveWorksheetResponse2(
                request = request,
                newWbState = wbStateMs,
                newActiveWindowPointer = newWindowPointer,
                newActiveWbPointer = newActiveWbPointer,
            )
        )
        return rt
    }

    override fun setActiveWsWithIndex(request: SetActiveWorksheetWithIndexRequest): RseNav<SetActiveWorksheetResponse2> {
        val wbk = request.wbKey
        val wbNameRs = appState.getWbRs(wbk).flatMap { it.getWsRs(request.wsIndex) }.mapError { it.withNav(wbKey =wbk) }
        val z = wbNameRs.flatMap {
            val req = SetActiveWorksheetRequest(wbk,it.name)
            setActiveWs(req)
        }
        return z
    }
}
