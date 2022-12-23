package com.qxdzbc.p6.ui.app.state

import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
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
import com.qxdzbc.p6.ui.format.CellFormatFlyweightTable
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.state.TextSizeSelectorState
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState

/**
 * An abstraction layer providing functions for looking up view states that is enclosed inside [AppState]
 */
interface SubAppStateContainer {

    fun getToolbarStateMs(windowId: String):Ms<ToolBarState>?
    fun getToolbarState(windowId: String):ToolBarState?

    fun getTextSizeSelectorStateMs(windowId:String):Ms<TextSizeSelectorState>?
    fun getTextSizeSelectorState(windowId:String):TextSizeSelectorState?

    fun getCellStateMsRs(wbwsSt:WbWsSt,cellAddress: CellAddress):Rse<Ms<CellState>>
    fun getCellStateMsRs(cellId:CellId):Rse<Ms<CellState>>

    fun getCellStateMs(wbwsSt:WbWsSt,cellAddress: CellAddress):Ms<CellState>?
    fun getCellState(wbwsSt:WbWsSt,cellAddress: CellAddress):CellState?

    fun getCellStateMs(cellId:CellId):Ms<CellState>?
    fun getCellState(cellId:CellId):CellState?

    val formatTableMs: Ms<CellFormatFlyweightTable>

    val windowStateMapMs: Ms<Map<String, Ms<OuterWindowState>>>
    var windowStateMap: Map<String, Ms<OuterWindowState>>

    val outerWindowStateMsList: List<Ms<OuterWindowState>>
    val windowStateMsList: List<Ms<WindowState>>

    val wbStateContMs: Ms<WorkbookStateContainer>
    var wbStateCont: WorkbookStateContainer

    fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2>

    /**
     * create and add a new wb state for [wb] if it yet to have a state of its own
     */
    fun addWbStateFor(wb: Workbook): SubAppStateContainer

    fun removeWindowState(windowState: Ms<WindowState>): SubAppStateContainer
    fun removeWindowState(windowId: String): SubAppStateContainer
    fun addWindowState(windowState: Ms<WindowState>): SubAppStateContainer
    fun createNewWindowStateMs(): Pair<SubAppStateContainer, Ms<OuterWindowState>>
    fun createNewWindowStateMs(windowId: String): Pair<SubAppStateContainer, Ms<OuterWindowState>>

    fun getWbStateMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<WorkbookState>>
    fun getWbStateMs(wbKeySt: St<WorkbookKey>): Ms<WorkbookState>?
    fun getWbStateRs(wbKeySt: St<WorkbookKey>): Rse<WorkbookState>
    fun getWbState(wbKeySt: St<WorkbookKey>): WorkbookState?

    fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>>
    fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState>
    fun getWbStateMs(wbKey: WorkbookKey): Ms<WorkbookState>?
    fun getWbState(wbKey: WorkbookKey): WorkbookState?

    fun getWsStateMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<Ms<WorksheetState>>
    fun getWsStateRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<WorksheetState>
    fun getWsStateMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<WorksheetState>?
    fun getWsState(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): WorksheetState?

    fun getWsStateMsRs(wbwsSt: WbWsSt): Rse<Ms<WorksheetState>>
    fun getWsStateRs(wbwsSt: WbWsSt): Rse<WorksheetState>
    fun getWsStateMs(wbwsSt: WbWsSt): Ms<WorksheetState>?
    fun getWsState(wbwsSt: WbWsSt): WorksheetState?

    fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>>
    fun getWsStateRs(wbKey: WorkbookKey, wsName: String): Rse<WorksheetState>
    fun getWsStateMs(wbKey: WorkbookKey, wsName: String): Ms<WorksheetState>?
    fun getWsState(wbKey: WorkbookKey, wsName: String): WorksheetState?

    fun getWsStateMsRs(wbws: WbWs): Rse<Ms<WorksheetState>>
    fun getWsStateRs(wbws: WbWs): Rse<WorksheetState>
    fun getWsStateMs(wbws: WbWs): Ms<WorksheetState>?
    fun getWsState(wbws: WbWs): WorksheetState?

    fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, ErrorReport>
    fun getWindowStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowState>?

    fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, ErrorReport>
    fun getFocusStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowFocusState>?

    fun getWindowStateByIdRs(windowId: String): Rse<WindowState>
    fun getWindowStateById(windowId: String): WindowState?
    fun getWindowStateByWbKeyRs(wbKey: WorkbookKey): Rse<WindowState>
    fun getWindowStateByWbKey(wbKey: WorkbookKey): WindowState?

    fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, ErrorReport>
    fun getWindowStateMsById(windowId: String): Ms<WindowState>?

    fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>?
    fun getCursorState(wbKey: WorkbookKey, wsName: String): CursorState?
    fun getCursorStateMs(wbws: WbWs): Ms<CursorState>?
    fun getCursorStateMs(wbwsSt: WbWsSt): Ms<CursorState>?
    fun getCursorState(wbws: WbWs): CursorState?
    fun getCursorState(wbws: WbWsSt): CursorState?

    /**
     * get cursor state ms of the active worksheet inside the workbook whose key is [wbKey]
     */
    fun getActiveCursorMs(wbKey: WorkbookKey): Ms<CursorState>?
    /**
     * get cursor state ms of the active worksheet inside the workbook whose key is [wbKey]
     */
    fun getActiveCursorMs(wbKeyMs: Ms<WorkbookKey>): Ms<CursorState>?

    fun addOuterWindowState(windowState: Ms<OuterWindowState>): SubAppStateContainer
    fun removeOuterWindowState(windowState: Ms<OuterWindowState>): SubAppStateContainer

    fun getRulerStateMsRs(wbws: WbWs, type: RulerType): Rse<Ms<RulerState>>
    fun getRulerStateMsRs(wbwsSt: WbWsSt, type: RulerType): Rse<Ms<RulerState>>
    fun getRulerStateMsRs(rulerSig: RulerSig): Rse<Ms<RulerState>>

    fun getRulerStateMs(wbws: WbWs, type: RulerType): Ms<RulerState>?
    fun getRulerStateMs(wbwsSt: WbWsSt, type: RulerType): Ms<RulerState>?
    fun getRulerStateMs(rulerSig: RulerSig): Ms<RulerState>?

    fun getRulerState(wbws: WbWs, type: RulerType): RulerState?
    fun getRulerState(wbwsSt: WbWsSt, type: RulerType): RulerState?
    fun getRulerState(rulerSig: RulerSig): RulerState?

    fun getSliderMsRs(wbwsSt: WbWsSt): Rse<Ms<GridSlider>>
    fun getSliderMs(wbwsSt: WbWsSt): Ms<GridSlider>?

    fun getThumbStateMsRs(wbwsSt:WbWsSt):Rse<Ms<ThumbState>>
    fun getThumbStateMs(wbwsSt:WbWsSt):Ms<ThumbState>?
}
