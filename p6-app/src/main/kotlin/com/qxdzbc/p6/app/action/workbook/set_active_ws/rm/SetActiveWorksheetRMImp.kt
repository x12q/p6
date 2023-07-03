package com.qxdzbc.p6.app.action.workbook.set_active_ws.rm

import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetWithIndexRequest
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.mapError
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWorksheetRMImp @Inject constructor(
    private val subAppStateContainer: StateContainer,
    private val activeWindowPointer:ActiveWindowPointer,
    private val docCont: DocumentContainer,
) : SetActiveWorksheetRM {

    private val dc = docCont
    private var subAppStateCont = subAppStateContainer

    override fun setActiveWs(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2> {
        val wbk = request.wbKey
        val wbStateMs = subAppStateCont
            .getWbStateMs(wbk)?.value
            ?.setActiveSheet(request.wsName)
        val wdState = subAppStateCont.getWindowStateMsByWbKey(wbk)
        activeWindowPointer
            .pointTo(wdState?.id)
        val newActiveWbPointer = wdState
            ?.activeWbPointer
            ?.pointTo(wbStateMs?.wbKeyMs)
        val rt = Ok(
            SetActiveWorksheetResponse2(
                request = request,
                newWbState = wbStateMs,
                newActiveWindowPointer = activeWindowPointer,
                newActiveWbPointer = newActiveWbPointer,
            )
        )
        return rt
    }

    override fun setActiveWsWithIndex(request: SetActiveWorksheetWithIndexRequest): RseNav<SetActiveWorksheetResponse2> {
        val wbk = request.wbKey
        val wbNameRs = dc.getWbRs(wbk).flatMap { it.getWsRs(request.wsIndex) }.mapError { it.withNav(wbKey =wbk) }
        val z = wbNameRs.flatMap {
            val req = SetActiveWorksheetRequest(wbk,it.name)
            setActiveWs(req)
        }
        return z
    }
}
