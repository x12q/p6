package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.state.app_state.AppWindowStateListMs
import com.qxdzbc.p6.di.state.app_state.WbStateContMs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.qxdzbc.p6.ui.window.state.WindowStateFactory.Companion.createDefault
import com.github.michaelbull.result.*
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.di.state.app_state.AppOuterWindowStateListMs
import com.qxdzbc.p6.rpc.workbook.msg.WorkbookKeyWithErrorResponse
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.OuterWindowStateFactory
import javax.inject.Inject


data class SubAppStateContainerImp @Inject constructor(
    @AppWindowStateListMs
    override val windowStateMsListMs: Ms<List<Ms<OuterWindowState>>>,
    @WbStateContMs
    override val wbStateContMs: Ms<WorkbookStateContainer>,
    private val windowStateFactory: WindowStateFactory,
    private val oWindowStateFactory: OuterWindowStateFactory,
    private val wbStateFactory: WorkbookStateFactory,
) : AbsSubAppStateContainer() {

    override var windowStateMsList: List<Ms<OuterWindowState>> by windowStateMsListMs
    override var wbStateCont: WorkbookStateContainer by wbStateContMs

    private fun hasStateFor(wbKey: WorkbookKey): Boolean {
        return this.getWbState(wbKey) != null
    }

    /**
     * Get a set of states related to a workbook key
     */
    override fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2> {
        val windowStateMsRs = this.getWindowStateMsByWbKeyRs(workbookKey)
        val rt = windowStateMsRs.flatMap { windowstateMs ->
            getWbStateMsRs(workbookKey).flatMap {
                QueryByWorkbookKeyResult2(
                    windowStateMs = windowstateMs,
                    workbookStateMs = it
                ).toOk()
            }
        }
        return rt
    }

    override fun addWbStateFor(wb: Workbook): SubAppStateContainer {
        if (this.hasStateFor(wb.key)) {
            return this
        } else {
            val newState = wbStateFactory.create(ms(wb))
            wbStateCont = wbStateCont.addOrOverwriteWbState(ms(newState))
            return this
        }
    }

    private val windowStateMap: Map<String, Ms<WindowState>>
        get() {
            return windowStateMsList
                .associateBy { it.value.innerWindowState.id }
                .map{(k,v)->k to v.value.innerWindowStateMs}
                .toMap()
        }

    override fun createNewWindowStateMs(): Pair<SubAppStateContainer, Ms<OuterWindowState>> {
        val newWindowState: Ms<WindowState> = ms(
            windowStateFactory.createDefault()
        )
        val o:Ms<OuterWindowState> = oWindowStateFactory.create(newWindowState).toMs()
        val newAppState = this.addOuterWindowState(o)
        return Pair(newAppState, o)
    }

    override fun createNewWindowStateMs(windowId: String): Pair<SubAppStateContainer, Ms<OuterWindowState>> {
        val newWindowState: Ms<WindowState> = ms(
            windowStateFactory.createDefault(id = windowId)
        )
        val o:Ms<OuterWindowState> = oWindowStateFactory.create(newWindowState).toMs()
        val newCont = this.addOuterWindowState(o)
        return Pair(newCont, o)
    }

    override fun removeWindowState(windowState: Ms<WindowState>): SubAppStateContainer {
        windowStateMsList = windowStateMsList.filter{it.value.innerWindowState!=windowState}
        return this
    }

    override fun removeOuterWindowState(windowState: Ms<OuterWindowState>): SubAppStateContainer {
        windowStateMsList = windowStateMsList - windowState
        return this
    }

    override fun removeWindowState(windowId: String): SubAppStateContainer {
        windowStateMsList = windowStateMsList.filter { it.value.id != windowId }
        return this
    }

    override fun addWindowState(windowState: Ms<WindowState>): SubAppStateContainer {
        windowStateMsList = windowStateMsList + ms(this.oWindowStateFactory.create(windowState))
        return this
    }
    override fun addOuterWindowState(windowState: Ms<OuterWindowState>): SubAppStateContainer {
        windowStateMsList = windowStateMsList + windowState
        return this
    }

    override fun getWindowStateMsById(windowId: String): Ms<WindowState>? {
        return windowStateMsList.firstOrNull{it.value.id == windowId}?.value?.innerWindowStateMs
    }

    override fun getWbStateMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<WorkbookState>> {
        return this.wbStateCont.getWbStateMsRs(wbKeySt)
    }

    override fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>> {
        return this.wbStateCont.getWbStateMsRs(wbKey)
    }

    override fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, ErrorReport> {
        val w = this.windowStateMsList.firstOrNull { it.value.innerWindowState.containWbKey(wbKey) }?.value?.innerWindowStateMs
        if (w != null) {
            return Ok(w)
        } else {
            return Err(AppStateErrors.InvalidWindowState.report1(wbKey))
        }
    }

    override fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, ErrorReport> {
        val w = windowStateMsList.firstOrNull { it.value.id == windowId }?.value?.innerWindowStateMs
        if(w!=null){
            return w.toOk()
        }else{
            return Err(AppStateErrors.InvalidWindowState.report2(windowId))
        }
    }
}
