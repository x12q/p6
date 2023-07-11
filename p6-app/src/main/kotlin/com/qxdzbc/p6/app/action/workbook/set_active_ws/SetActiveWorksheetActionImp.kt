package com.qxdzbc.p6.app.action.workbook.set_active_ws

import com.github.michaelbull.result.*
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWorksheetActionImp @Inject constructor(
    private val subAppStateContainer: StateContainer,
    private val activeWindowPointer: ActiveWindowPointer,
    private val docCont: DocumentContainer,
    private val errorRouter: ErrorRouter,
) : SetActiveWorksheetAction {

    private val dc = docCont
    private val subAppStateCont = subAppStateContainer

    override fun setActiveWs(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2> {
        val r1 = makeRequest(request)
        val r2 = apply(r1)
        return r2
    }

    override fun setActiveWsUsingIndex(request: SetActiveWorksheetWithIndexRequest): RseNav<SetActiveWorksheetResponse2> {
        val r1 = setActiveWsWithIndex(request)
        val r2 = apply(r1)
        return r2
    }
    fun apply(res: RseNav<SetActiveWorksheetResponse2>): RseNav<SetActiveWorksheetResponse2> {
        res.onSuccess {rs:SetActiveWorksheetResponse2->
            val k = rs.request.wbKey

            rs.newActiveWbPointer?.also {
                val wdStateMs = subAppStateContainer.getWindowStateMsByWbKey(k)
                if(wdStateMs!=null){
                    wdStateMs.activeWbPointerMs.value = it
                }
            }

        }.onFailure {
            errorRouter.publish(it)
        }
        return res
    }

    fun makeRequest(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2> {
        val wbk = request.wbKey
        val wbState = subAppStateCont.getWbState(wbk)
        wbState?.setActiveSheet(request.wsName)
        val wdState = subAppStateCont.getWindowStateMsByWbKey(wbk)
        activeWindowPointer
            .pointTo(wdState?.id)
        val newActiveWbPointer = wdState
            ?.activeWbPointer
            ?.pointTo(wbState?.wbKeyMs)
        val rt = Ok(
            SetActiveWorksheetResponse2(
                request = request,
                newWbState = wbState,
                newActiveWindowPointer = activeWindowPointer,
                newActiveWbPointer = newActiveWbPointer,
            )
        )
        return rt
    }

    fun setActiveWsWithIndex(request: SetActiveWorksheetWithIndexRequest): RseNav<SetActiveWorksheetResponse2> {
        val wbk = request.wbKey
        val wbNameRs = dc.getWbRs(wbk).flatMap { it.getWsRs(request.wsIndex) }.mapError { it.withNav(wbKey =wbk) }
        val z = wbNameRs.flatMap {
            val req = SetActiveWorksheetRequest(wbk,it.name)
            setActiveWs(req)
        }
        return z
    }
}
