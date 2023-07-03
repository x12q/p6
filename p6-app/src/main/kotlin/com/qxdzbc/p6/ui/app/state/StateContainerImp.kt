package com.qxdzbc.p6.ui.app.state


import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.*
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.ResultUtils.toRs
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetErrors
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
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
import java.nio.file.Path
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType = StateContainer::class)
class StateContainerImp @Inject constructor(
    private val docCont: DocumentContainer,
    override val windowStateMapMs: Ms<Map<String, Ms<OuterWindowState>>>,
    override val wbStateContMs: Ms<WorkbookStateContainer>,
    private val windowStateFactory: WindowStateFactory,
    private val outerWindowStateFactory: OuterWindowStateFactory,
    private val wbStateFactory: WorkbookStateFactory,
    override val activeWindowPointer: ActiveWindowPointer,
    override val cellEditorStateMs: Ms<CellEditorState>,
) : AbsStateContainer(),DocumentContainer by docCont {

    override fun getActiveWorkbook(): Workbook? {
        return getActiveWindowState()?.activeWbState?.wb
    }

    override fun getActiveWorkbookRs(): Rse<Workbook> {
        val wbk: Workbook? = getActiveWindowState()?.activeWbState?.wb
        return wbk.toRs(AppStateErrors.NoActiveWorkbook.report())
    }

    override var wbCont: WorkbookContainer by wbContMs

    override fun getActiveWindowStateMs(): Ms<WindowState>? {
        return activeWindowPointer.windowId?.let {
            getWindowStateMsById(it)
        }
    }

    override fun getActiveWindowState(): WindowState? {
        return getActiveWindowStateMs()?.value
    }

    override var cellEditorState: CellEditorState by cellEditorStateMs

    override fun getWindowStateMs_OrDefault_OrCreateANewOne_Rs(windowId: String?): Rse<Ms<WindowState>> {
        val windowMsRs: Rse<Ms<WindowState>> = if (windowId != null) {
            val q: Rse<Ms<WindowState>> = getWindowStateMsByIdRs(windowId)
            q
        } else {
            val activeWid = getActiveWindowStateMs()
            if (activeWid != null) {
                val q = Ok(activeWid)
                q
            } else {
                val firstWid = windowStateMsList.firstOrNull()
                if (firstWid != null) {
                    val q = Ok(firstWid)
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

    override fun getActiveWbStateMs(): Ms<WorkbookState>? {
        return getActiveWindowState()?.activeWbStateMs
    }

    override fun getActiveWbState(): WorkbookState? {
        return getActiveWindowState()?.activeWbStateMs?.value
    }

    override var windowStateMap: Map<String, Ms<OuterWindowState>> by windowStateMapMs

    override val outerWindowStateMsList: List<Ms<OuterWindowState>>
        get() = windowStateMap.values.toList()

    override var wbStateCont: WorkbookStateContainer by wbStateContMs

    override val windowStateMsList: List<Ms<WindowState>> get() = outerWindowStateMsList.map { it.value.innerWindowStateMs }

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

    override fun getStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult2? {
        return getStateByWorkbookKeyRs(workbookKey).component1()
    }

    override fun addWbStateFor(wb: Workbook) {
        if (!this.hasStateFor(wb.key)) {
            val newState = wbStateFactory.create(StateUtils.ms(wb))
            wbStateCont = wbStateCont.addOrOverwriteWbState(StateUtils.ms(newState))
        }
    }

    override fun createNewWindowStateMs(): Ms<OuterWindowState> {
        val newWindowState: Ms<WindowState> = StateUtils.ms(
            windowStateFactory.createDefault()
        )
        val o: Ms<OuterWindowState> = outerWindowStateFactory.create(newWindowState).toMs()
        this.addOuterWindowState(o)
        return o
    }

    override fun createNewWindowStateMs(windowId: String): Ms<OuterWindowState> {
        val newWindowState: Ms<WindowState> = StateUtils.ms(
            windowStateFactory.createDefault(id = windowId)
        )
        val o: Ms<OuterWindowState> = outerWindowStateFactory.create(newWindowState).toMs()
        this.addOuterWindowState(o)
        return o
    }

    override fun removeWindowState(windowState: Ms<WindowState>) {
        windowStateMap = windowStateMap.filter { (id, oStateMs) ->
            oStateMs.value.innerWindowStateMs != windowState
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
        return this.getWsStateMsRs(wbwsSt).flatMap {
            Ok(it.value.cursorStateMs.value.thumbStateMs)
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

    override fun addWindowState(windowState: Ms<WindowState>) {
        windowStateMap = windowStateMap + (windowState.value.id to StateUtils.ms(
            this.outerWindowStateFactory.create(
                windowState
            )
        ))
    }

    override fun addOuterWindowState(windowState: Ms<OuterWindowState>) {
        windowStateMap = windowStateMap + (windowState.value.windowId to windowState)
    }

    override fun getWindowStateMsById(windowId: String): Ms<WindowState>? {
        return windowStateMap[windowId]?.value?.innerWindowStateMs
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
        return getCellStateMsRs(cellId,cellId.address)
    }

    override fun getWbStateMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<WorkbookState>> {
        return this.wbStateCont.getWbStateMsRs(wbKeySt)
    }

    override fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>> {
        return this.wbStateCont.getWbStateMsRs(wbKey)
    }

    override fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, SingleErrorReport> {
        val w = windowStateMap.values.firstOrNull { owds ->
            owds.value.innerWindowState.containWbKey(wbKey)
        }?.value?.innerWindowStateMs
        if (w != null) {
            return Ok(w)
        } else {
            return Err(AppStateErrors.InvalidWindowState.report1(wbKey))
        }
    }

    override fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, SingleErrorReport> {
        val w = windowStateMap[windowId]?.value?.innerWindowStateMs
        if (w != null) {
            return w.toOk()
        } else {
            return Err(AppStateErrors.InvalidWindowState.report2(windowId))
        }
    }
}
