package com.qxdzbc.p6.ui.app.state


import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.ResultUtils.toRs
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
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
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerSig
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState
import java.nio.file.Path
import javax.inject.Inject

class StateContainerImp @Inject constructor(
    override val appStateMs: Ms<AppState>,
    val docContMs: Ms<DocumentContainer>,
    val subAppStateContMs: Ms<SubAppStateContainer>,
) : StateContainer, AbsSubAppStateContainer() {

    private var subAppStateCont by subAppStateContMs
    override var appState by appStateMs

    override fun getActiveWorkbook(): Workbook? {
        return appState.activeWindowState?.activeWbState?.wb
    }

    override fun getActiveWorkbookRs(): Rse<Workbook> {
        val wbk: Workbook? = appState.activeWindowState?.activeWbState?.wb
        return wbk.toRs(AppStateErrors.NoActiveWorkbook.report())
    }

    private val docCont by docContMs
    override val wbContMs: Ms<WorkbookContainer>
        get() = docCont.wbContMs
    override var wbCont: WorkbookContainer by wbContMs
    override fun getActiveWindowStateMs(): Ms<WindowState>? {
        return appState.activeWindowStateMs
    }

    override fun getActiveWindowState(): WindowState? {
        return appState.activeWindowState
    }

    override val cellEditorStateMs: Ms<CellEditorState>
        get() = appState.cellEditorStateMs
    override var cellEditorState: CellEditorState by cellEditorStateMs

    override val windowStateMsList: List<Ms<WindowState>> get() = subAppStateCont.windowStateMsList

    override val wbStateContMs: Ms<WorkbookStateContainer>
        get() = subAppStateCont.wbStateContMs
    override var wbStateCont: WorkbookStateContainer by wbStateContMs

    override fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2> {
        return subAppStateCont.getStateByWorkbookKeyRs(workbookKey)
    }

    override fun addWbStateFor(wb: Workbook): StateContainer {
        subAppStateCont = subAppStateCont.addWbStateFor(wb)
        return this
    }

    override fun removeWindowState(windowState: Ms<WindowState>): StateContainer {
        subAppStateCont = subAppStateCont.removeWindowState(windowState)
        return this
    }

    override fun removeWindowState(windowId: String): StateContainer {
        subAppStateCont = subAppStateCont.removeWindowState(windowId)
        return this
    }

    override fun addWindowState(windowState: Ms<WindowState>): StateContainer {
        subAppStateCont = subAppStateCont.addWindowState(windowState)
        return this
    }

    override fun createNewWindowStateMs(): Pair<StateContainer, Ms<OuterWindowState>> {
        val o = subAppStateCont.createNewWindowStateMs()
        subAppStateCont = o.first
        return this to o.second
    }

    override fun createNewWindowStateMs(windowId: String): Pair<StateContainer, Ms<OuterWindowState>> {
        val o = subAppStateCont.createNewWindowStateMs(windowId)
        subAppStateCont = o.first
        return this to o.second
    }

    override fun getWbStateMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<WorkbookState>> {
        return subAppStateCont.getWbStateMsRs(wbKeySt)
    }

    override fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>> {
        return subAppStateCont.getWbStateMsRs(wbKey)
    }

    override fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>> {
        return subAppStateCont.getWsStateMsRs(wbKey, wsName)
    }

    override fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, SingleErrorReport> {
        return subAppStateCont.getWindowStateMsByWbKeyRs(wbKey)
    }

    override fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, SingleErrorReport> {
        return subAppStateCont.getFocusStateMsByWbKeyRs(wbKey)
    }

    override fun getCellStateMsRs(wbwsSt: WbWsSt, cellAddress: CellAddress): Rse<Ms<CellState>> {
        return subAppStateCont.getCellStateMsRs(wbwsSt, cellAddress)
    }

    override fun getCellStateMsRs(cellId: CellId): Rse<Ms<CellState>> {
        return subAppStateCont.getCellStateMsRs(cellId)
    }

    override var windowStateMap: Map<String, Ms<OuterWindowState>> by windowStateMapMs

    override val outerWindowStateMsList: List<Ms<OuterWindowState>>
        get() = subAppStateCont.outerWindowStateMsList

    override val windowStateMapMs: Ms<Map<String, Ms<OuterWindowState>>>
        get() = this.subAppStateCont.windowStateMapMs

    override fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, SingleErrorReport> {
        return subAppStateCont.getWindowStateMsByIdRs(windowId)
    }

    override fun addOuterWindowState(windowState: Ms<OuterWindowState>): StateContainerImp {
        subAppStateCont = subAppStateCont.addOuterWindowState(windowState)
        return this
    }

    override fun removeOuterWindowState(windowState: Ms<OuterWindowState>): StateContainerImp {
        subAppStateCont = subAppStateCont.removeOuterWindowState(windowState)
        return this
    }

    override fun getRulerStateMsRs(wbws: WbWs, type: RulerType): Rse<Ms<RulerState>> {
        return subAppStateCont.getRulerStateMsRs(wbws, type)
    }

    override fun getRulerStateMsRs(wbwsSt: WbWsSt, type: RulerType): Rse<Ms<RulerState>> {
        return subAppStateCont.getRulerStateMsRs(wbwsSt, type)
    }

    override fun getRulerStateMsRs(rulerSig: RulerSig): Rse<Ms<RulerState>> {
        return subAppStateCont.getRulerStateMsRs(rulerSig)
    }

    override fun getRulerStateMs(wbws: WbWs, type: RulerType): Ms<RulerState>? {
        return subAppStateCont.getRulerStateMs(wbws, type)
    }

    override fun getRulerStateMs(wbwsSt: WbWsSt, type: RulerType): Ms<RulerState>? {
        return subAppStateCont.getRulerStateMs(wbwsSt, type)
    }

    override fun getRulerStateMs(rulerSig: RulerSig): Ms<RulerState>? {
        return subAppStateCont.getRulerStateMs(rulerSig)
    }

    override fun getRulerState(wbws: WbWs, type: RulerType): RulerState? {
        return subAppStateCont.getRulerState(wbws, type)
    }

    override fun getRulerState(wbwsSt: WbWsSt, type: RulerType): RulerState? {
        return subAppStateCont.getRulerState(wbwsSt, type)
    }

    override fun getRulerState(rulerSig: RulerSig): RulerState? {
        return subAppStateCont.getRulerState(rulerSig)
    }

    override fun getSliderMsRs(wbwsSt: WbWsSt): Rse<Ms<GridSlider>> {
        return subAppStateCont.getSliderMsRs(wbwsSt)
    }

    override fun getSliderMs(wbwsSt: WbWsSt): Ms<GridSlider>? {
        return subAppStateCont.getSliderMs(wbwsSt)
    }

    override fun getThumbStateMsRs(wbwsSt: WbWsSt): Rse<Ms<ThumbState>> {
        return subAppStateCont.getThumbStateMsRs(wbwsSt)
    }

    override fun getThumbStateMs(wbwsSt: WbWsSt): Ms<ThumbState>? {
        return subAppStateCont.getThumbStateMs(wbwsSt)
    }

    override fun getWindowStateMsDefaultRs(windowId: String?): Rse<Ms<WindowState>> {
        val windowMsRs: Rse<Ms<WindowState>> = if (windowId != null) {
            val q: Rse<Ms<WindowState>> = getWindowStateMsByIdRs(windowId)
            q
        } else {
            val activeWid = appState.activeWindowStateMs
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
        val rt = this.appState.activeWindowState?.activeWbPointer?.wbKeyMs?.let {
            this.getActiveCursorMs(it)
        }
        return rt
    }

    override fun getActiveCursorState(): CursorState? {
        return getActiveCursorStateMs()?.value
    }

    override fun getActiveWbStateMs(): Ms<WorkbookState>? {
        return appState.activeWindowState?.activeWbStateMs
    }

    override fun getActiveWbState(): WorkbookState? {
        return appState.activeWindowState?.activeWbStateMs?.value
    }

    override fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>? {
        return subAppStateCont.getCursorStateMs(wbKey, wsName)
    }

    override fun getWbWsSt(wbKey: WorkbookKey, wsName: String): WbWsSt? {
        return docCont.getWbWsSt(wbKey, wsName)
    }

    override fun getWbWsSt(wbWs: WbWs): WbWsSt? {
        return docCont.getWbWsSt(wbWs)
    }

    override fun getWbKeySt(wbKey: WorkbookKey): St<WorkbookKey>? {
        return docCont.getWbKeySt(wbKey)
    }

    override fun getWbKeyStRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>> {
        return docCont.getWbKeyStRs(wbKey)
    }

    override fun getWbKeyMs(wbKey: WorkbookKey): Ms<WorkbookKey>? {
        return docCont.getWbKeyMs(wbKey)
    }

    override fun getWbKeyMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>> {
        return docCont.getWbKeyMsRs(wbKey)
    }

    override fun getWsNameSt(wbKey: WorkbookKey, wsName: String): St<String>? {
        return docCont.getWsNameSt(wbKey, wsName)
    }

    override fun getWsNameSt(wbws: WbWs): St<String>? {
        return docCont.getWsNameSt(wbws)
    }

    override fun getWsNameSt(wbKeySt: St<WorkbookKey>, wsName: String): St<String>? {
        return docCont.getWsNameSt(wbKeySt, wsName)
    }

    override fun getWsNameStRs(wbws: WbWs): Rse<St<String>> {
        return docCont.getWsNameStRs(wbws)
    }

    override fun getWsNameMs(wbKey: WorkbookKey, wsName: String): Ms<String>? {
        return docCont.getWsNameMs(wbKey, wsName)
    }

    override fun getWsNameMs(wbKeySt: St<WorkbookKey>, wsName: String): Ms<String>? {
        return docCont.getWsNameMs(wbKeySt, wsName)
    }

    override fun getWbWsStRs(wbWs: WbWs): Rse<WbWsSt> {
        return docCont.getWbWsStRs(wbWs)
    }

    override fun getWbRs(wbKey: WorkbookKey): Rse<Workbook> {
        return docCont.getWbRs(wbKey)
    }

    override fun getWbRs(path: Path): Rse<Workbook> {
        return docCont.getWbRs(path)
    }

    override fun getWbMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<Workbook>> {
        return docCont.getWbMsRs(wbKeySt)
    }

    override fun getWbMsRs(wbKey: WorkbookKey): Rse<Ms<Workbook>> {
        return docCont.getWbMsRs(wbKey)
    }

    override fun getWbMsRs(path: Path): Rse<Ms<Workbook>> {
        return docCont.getWbMsRs(path)
    }

    override fun getWb(wbKey: WorkbookKey): Workbook? {
        return docCont.getWb(wbKey)
    }

    override fun getWb(path: Path): Workbook? {
        return docCont.getWb(path)
    }

    override fun getWbMs(wbKeySt: St<WorkbookKey>): Ms<Workbook>? {
        return docCont.getWbMs(wbKeySt)
    }

    override fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>? {
        return docCont.getWbMs(wbKey)
    }

    override fun getWbRs(wbKeySt: St<WorkbookKey>): Rse<Workbook> {
        return docCont.getWbRs(wbKeySt)
    }

    override fun getWsRs(wbKey: WorkbookKey, wsName: String): Rse<Worksheet> {
        return docCont.getWsRs(wbKey, wsName)
    }

    override fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<Worksheet> {
        return docCont.getWsRs(wbKeySt, wsNameSt)
    }

    override fun getWsRs(wbwsSt: WbWsSt): Rse<Worksheet> {
        return docCont.getWsRs(wbwsSt)
    }

    override fun getWsRs(wbws: WbWs): Rse<Worksheet> {
        return docCont.getWsRs(wbws)
    }

    override fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet? {
        return docCont.getWs(wbKey, wsName)
    }

    override fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet? {
        return docCont.getWs(wbKeySt, wsNameSt)
    }

    override fun getWs(wbws: WbWs): Worksheet? {
        return docCont.getWs(wbws)
    }

    override fun getWs(wbwsSt: WbWsSt): Worksheet? {
        return docCont.getWs(wbwsSt)
    }

    override fun getWs(wsId: WorksheetIdWithIndexPrt): Worksheet? {
        return docCont.getWs(wsId)
    }

    override fun getWsMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<Worksheet>> {
        return docCont.getWsMsRs(wbKey, wsName)
    }

    override fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<Ms<Worksheet>> {
        return docCont.getWsMsRs(wbKeySt, wsNameSt)
    }

    override fun getWsMsRs(wbwsSt: WbWsSt): Rse<Ms<Worksheet>> {
        return docCont.getWsMsRs(wbwsSt)
    }

    override fun getWsMsRs(wbws: WbWs): Rse<Ms<Worksheet>> {
        return docCont.getWsMsRs(wbws)
    }

    override fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>? {
        return docCont.getWsMs(wbKey, wsName)
    }

    override fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>? {
        return docCont.getWsMs(wbKeySt, wsNameSt)
    }

    override fun getWsMs(wbws: WbWs): Ms<Worksheet>? {
        return docCont.getWsMs(wbws)
    }

    override fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>? {
        return docCont.getWsMs(wbwsSt)
    }

    override fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rse<Range> {
        return docCont.getRangeRs(wbKey, wsName, rangeAddress)
    }

    override fun getRangeRs(rangeId: RangeId): Rse<Range> {
        return docCont.getRangeRs(rangeId)
    }

    override fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range? {
        return docCont.getRange(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range? {
        return docCont.getLazyRange(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRangeRs(
        wbKey: WorkbookKey,
        wsName: String,
        rangeAddress: RangeAddress
    ): Rse<Range> {
        return docCont.getLazyRangeRs(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRangeRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        rangeAddress: RangeAddress
    ): Rse<Range> {
        return docCont.getLazyRangeRs(wbKeySt, wsNameSt, rangeAddress)
    }

    override fun getCellRsOrDefault(
        wbKey: WorkbookKey,
        wsName: String,
        cellAddress: CellAddress
    ): Rse<Cell> {
        return docCont.getCellRsOrDefault(wbKey, wsName, cellAddress)
    }

    override fun getCellRsOrDefault(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rse<Cell> {
        return docCont.getCellRsOrDefault(wbKeySt, wsNameSt, cellAddress)
    }

    override fun getCellRsOrDefault(cellId: CellIdDM): Rse<Cell> {
        return docCont.getCellRsOrDefault(cellId)
    }

    override fun getCellRsOrDefault(cellId: CellId): Rse<Cell> {
        return docCont.getCellRsOrDefault(cellId)
    }

    override fun getCellOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell? {
        return docCont.getCellOrDefault(wbKey, wsName, cellAddress)
    }

    override fun getCellOrDefault(wbws: WbWs, cellAddress: CellAddress): Cell? {
        return docCont.getCellOrDefault(wbws, cellAddress)
    }

    override fun getCellOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>, cellAddress: CellAddress): Cell? {
        return docCont.getCellOrDefault(wbKeySt, wsNameSt, cellAddress)
    }

    override fun getCellOrDefault(wbwsSt: WbWsSt, cellAddress: CellAddress): Cell? {
        return docCont.getCellOrDefault(wbwsSt, cellAddress)
    }

    override fun getCellOrDefault(cellId: CellIdDM): Cell? {
        return docCont.getCellOrDefault(cellId)
    }

    override fun getCellOrDefault(cellId: CellId): Cell? {
        return docCont.getCellOrDefault(cellId)
    }

    override fun getCellMsRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rse<Ms<Cell>> {
        return docCont.getCellMsRs(wbKey, wsName, cellAddress)
    }

    override fun getCellMsRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rse<Ms<Cell>> {
        return docCont.getCellMsRs(wbKeySt, wsNameSt, cellAddress)
    }

    override fun getCellMsRs(cellId: CellIdDM): Rse<Ms<Cell>> {
        return docCont.getCellMsRs(cellId)
    }

    override fun getCellMs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Ms<Cell>? {
        return docCont.getCellMs(wbKey, wsName, cellAddress)
    }

    override fun getCellMs(cellIdDM: CellIdDM): Ms<Cell>? {
        return docCont.getCellMs(cellIdDM)
    }

    override fun getCellMs(cellId: CellId): Ms<Cell>? {
        return docCont.getCellMs(cellId)
    }

    override fun getCell(cellIdDM: CellIdDM): Cell? {
        return docCont.getCell(cellIdDM)
    }

    override fun getCell(cellId: CellId): Cell? {
        return docCont.getCell(cellId)
    }

    override fun getCellIdRs(cellIdDM: CellIdDM): Rse<CellId> {
        return docCont.getCellIdRs(cellIdDM)
    }

    override fun getCellId(cellIdDM: CellIdDM): CellId? {
        return docCont.getCellId(cellIdDM)
    }

    override fun replaceWb(newWb: Workbook): DocumentContainer {
        return docCont.replaceWb(newWb)
    }

    override val allWbs: List<Workbook>
        get() = docCont.allWbs
    override val allWbMs: List<Ms<Workbook>>
        get() = docCont.allWbMs

    override fun getWb(wbKeySt: St<WorkbookKey>): Workbook? {
        return docCont.getWb(wbKeySt)
    }
}
