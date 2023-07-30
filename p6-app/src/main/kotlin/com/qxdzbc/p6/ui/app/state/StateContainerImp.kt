package com.qxdzbc.p6.ui.app.state


import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.*
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.ResultUtils.toRs
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetErrors
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateGetter
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerSig
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.OuterWindowStateFactory
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.qxdzbc.p6.ui.window.state.WindowStateFactory.Companion.createDefault
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class, boundType = StateContainer::class)
class StateContainerImp @Inject constructor(
    private val docCont: DocumentContainer,
    override val windowStateMapMs: Ms<Map<String, Ms<OuterWindowState>>>,
    override val wbStateCont: WorkbookStateContainer,
    private val windowStateFactory: WindowStateFactory,
    private val outerWindowStateFactory: OuterWindowStateFactory,
    private val wbStateFactory: WorkbookStateFactory,
    override val activeWindowPointer: ActiveWindowPointer,
    override val cellEditorStateMs: Ms<CellEditorState>,
) : AbsStateContainer(), DocumentContainer by docCont, WorkbookStateGetter by wbStateCont {

    override fun getActiveWorkbook(): Workbook? {
        return getActiveWindowState()?.activeWbState?.wb
    }

    override fun getActiveWorkbookRs(): Rse<Workbook> {
        val wbk: Workbook? = getActiveWindowState()?.activeWbState?.wb
        return wbk.toRs(AppStateErrors.NoActiveWorkbook.report())
    }

    override fun getActiveWindowStateMs(): WindowState? {
        return activeWindowPointer.windowId?.let {
            getWindowStateMsById(it)
        }
    }

    override fun getActiveWindowState(): WindowState? {
        return getActiveWindowStateMs()
    }

    override var cellEditorState: CellEditorState by cellEditorStateMs

    override fun getWindowState_OrDefault_Rs(windowId: String?): Rse<WindowState> {
        val windowMsRs: Rse<WindowState> = if (windowId != null) {
            val q: Rse<WindowState> = getWindowStateMsByIdRs(windowId)
            q
        } else {
            val activeWindowState: WindowState? = getActiveWindowStateMs()
            if (activeWindowState != null) {
                val q = Ok(activeWindowState)
                q
            } else {
                val firstWindowState: WindowState? = windowStateMsList.firstOrNull()
                if (firstWindowState != null) {
                    val q = Ok(firstWindowState)
                    q
                } else {
                    Err(AppStateErrors.InvalidWindowState.report3("Unable to get a default window state"))
                }
            }
        }
        return windowMsRs
    }

    override fun getActiveCursorStateMs(): Ms<CursorState>? {
        val rt = getActiveWindowState()?.activeWbPointer?.wbKeyMs?.let {
            getActiveCursorMs(it)
        }
        return rt
    }

    override fun getActiveCursorState(): CursorState? {
        return getActiveCursorStateMs()?.value
    }

    override fun getActiveWbState(): WorkbookState? {
        return getActiveWindowState()?.activeWbStateMs
    }

    override var windowStateMap: Map<String, Ms<OuterWindowState>> by windowStateMapMs

    override val outerWindowStateMsList: List<Ms<OuterWindowState>>
        get() = windowStateMap.values.toList()

    override val windowStateMsList: List<WindowState>
        get() = outerWindowStateMsList.map {
            it.value.innerWindowState
        }

    private fun hasStateFor(wbKey: WorkbookKey): Boolean {
        return this.getWbState(wbKey) != null
    }

    /**
     * Get a set of states related to a workbook key
     */
    override fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult> {
        val windowStateMsRs = this.getWindowStateMsByWbKeyRs(workbookKey)
        val rt = windowStateMsRs.flatMap { windowstateMs ->
            getWbStateRs(workbookKey).flatMap {
                QueryByWorkbookKeyResult(
                    windowState = windowstateMs,
                    workbookStateMs = it
                ).toOk()
            }
        }
        return rt
    }

    override fun getStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult? {
        return getStateByWorkbookKeyRs(workbookKey).component1()
    }

    override fun addWbStateFor(wb: Workbook) {
        if (!this.hasStateFor(wb.key)) {
            val newState = wbStateFactory.create(ms(wb))
            wbStateCont.addOrOverwriteWbState(newState)
        }
    }

    override fun createNewWindowStateMs(): Ms<OuterWindowState> {
        val newWindowState: WindowState = windowStateFactory.createDefault()
        val o: Ms<OuterWindowState> = outerWindowStateFactory.create(newWindowState).toMs()
        this.addOuterWindowState(o)
        return o
    }

    override fun createNewWindowStateMs(windowId: String): Ms<OuterWindowState> {
        val newWindowState: WindowState = windowStateFactory.createDefault(id = windowId)

        val o: Ms<OuterWindowState> = outerWindowStateFactory.create(newWindowState).toMs()
        this.addOuterWindowState(o)
        return o
    }

    override fun removeWindowState(windowState: WindowState) {
        windowStateMap = windowStateMap.filter { (id, oStateMs) ->
            oStateMs.value.innerWindowState != windowState
        }
    }

    override fun removeOuterWindowState(windowState: Ms<OuterWindowState>) {
        windowStateMap = windowStateMap.filter { (id, oStateMs) ->
            oStateMs != windowState
        }
    }

    private fun getRulerStateZZ(wsState: WorksheetState, rulerType: RulerType): Ms<RulerState> {
        return when (rulerType) {
            RulerType.Row -> wsState.rowRulerStateMs
            RulerType.Col -> wsState.colRulerStateMs
        }
    }

    override fun getRulerStateMsRs(wbws: WbWs, type: RulerType): Rse<Ms<RulerState>> {
        return this.getWsStateRs(wbws).map {
            getRulerStateZZ(it, type)
        }
    }

    override fun getRulerStateMsRs(wbwsSt: WbWsSt, type: RulerType): Rse<Ms<RulerState>> {
        return this.getWsStateRs(wbwsSt).map {
            getRulerStateZZ(it, type)
        }
    }

    override fun getRulerStateMsRs(rulerSig: RulerSig): Rse<Ms<RulerState>> {
        return this.getRulerStateMsRs(rulerSig, rulerSig.type)
    }

    override fun getRulerStateMs(wbws: WbWs, type: RulerType): Ms<RulerState>? {
        return getRulerStateMsRs(wbws, type).component1()
    }

    override fun getRulerStateMs(wbwsSt: WbWsSt, type: RulerType): Ms<RulerState>? {
        return getRulerStateMsRs(wbwsSt, type).component1()
    }

    override fun getRulerStateMs(rulerSig: RulerSig): Ms<RulerState>? {
        return this.getRulerStateMs(rulerSig, rulerSig.type)
    }

    override fun getRulerState(wbws: WbWs, type: RulerType): RulerState? {
        return this.getRulerStateMs(wbws, type)?.value
    }

    override fun getRulerState(wbwsSt: WbWsSt, type: RulerType): RulerState? {
        return this.getRulerStateMs(wbwsSt, type)?.value
    }

    override fun getRulerState(rulerSig: RulerSig): RulerState? {
        return this.getRulerState(rulerSig, rulerSig.type)
    }

    override fun getSliderMsRs(wbwsSt: WbWsSt): Rse<Ms<GridSlider>> {
        return this.getWsStateRs(wbwsSt).map { it.sliderMs }
    }

    override fun getSliderMs(wbwsSt: WbWsSt): Ms<GridSlider>? {
        return this.getWsStateRs(wbwsSt).component1()?.sliderMs
    }

    override fun getThumbStateMsRs(wbwsSt: WbWsSt): Rse<Ms<ThumbState>> {
        return this.getWsStateRs(wbwsSt).flatMap {
            Ok(it.cursorStateMs.value.thumbStateMs)
        }
    }

    override fun getThumbStateMs(wbwsSt: WbWsSt): Ms<ThumbState>? {
        return getThumbStateMsRs(wbwsSt).component1()
    }

    override fun removeWindowState(windowId: String) {
        windowStateMap = windowStateMap.filter { (id, oStateMs) ->
            id != windowId
        }
    }

    override fun addWindowState(windowState: WindowState) {
        windowStateMap = windowStateMap + (windowState.id to StateUtils.ms(
            this.outerWindowStateFactory.create(
                windowState
            )
        ))
    }

    override fun addOuterWindowState(windowState: Ms<OuterWindowState>) {
        windowStateMap = windowStateMap + (windowState.value.windowId to windowState)
    }

    override fun getWindowStateMsById(windowId: String): WindowState? {
        return windowStateMap[windowId]?.value?.innerWindowState
    }

    override fun getCellStateMsRs(wbwsSt: WbWsSt, cellAddress: CellAddress): Rse<Ms<CellState>> {
        val rt = getWsStateRs(wbwsSt).flatMap {
            it.getCellStateMs(cellAddress)?.let { csMs ->
                Ok(csMs)
            } ?: WorksheetErrors.InvalidCell
                .report("can't get state for cell $cellAddress in $wbwsSt")
                .toErr()
        }
        return rt
    }

    override fun getCellStateMsRs(cellId: CellId): Rse<Ms<CellState>> {
        return getCellStateMsRs(cellId, cellId.address)
    }

    override fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<WindowState, SingleErrorReport> {
        val w = windowStateMap.values.firstOrNull { owds ->
            owds.value.innerWindowState.containWbKey(wbKey)
        }?.value?.innerWindowState
        if (w != null) {
            return Ok(w)
        } else {
            return Err(AppStateErrors.InvalidWindowState.report1(wbKey))
        }
    }

    override fun getWindowStateMsByIdRs(windowId: String): Rs<WindowState, SingleErrorReport> {
        val w = windowStateMap[windowId]?.value?.innerWindowState
        if (w != null) {
            return w.toOk()
        } else {
            return Err(AppStateErrors.InvalidWindowState.report2(windowId))
        }
    }
}
