package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState

abstract class AbsSubAppStateContainer : SubAppStateContainer {

    override fun getCellStateMs(cellId: CellId): Ms<CellState>? {
        return getCellStateMsRs(cellId).component1()
    }

    override fun getCellState(cellId: CellId): CellState? {
        return getCellStateMs(cellId)?.value
    }

    override fun getCellState(wbwsSt: WbWsSt, cellAddress: CellAddress): CellState? {
        return getCellStateMs(wbwsSt, cellAddress)?.value
    }

    override fun getCellStateMs(wbwsSt: WbWsSt, cellAddress: CellAddress): Ms<CellState>? {
        return getCellStateMsRs(wbwsSt,cellAddress).component1()
    }

    override fun getCursorStateMs(wbwsSt: WbWsSt): Ms<CursorState>? {
        return this.getWsState(wbwsSt)?.cursorStateMs
    }

    override fun getWindowStateByIdRs(windowId:String): Rse<WindowState> {
        return getWindowStateMsByIdRs(windowId).map { it.value }
    }
    override fun getWindowStateById(windowId:String): WindowState? {
        return getWindowStateMsById(windowId)?.value
    }
    override fun getWindowStateByWbKeyRs(wbKey: WorkbookKey): Rse<WindowState> {
        return getWindowStateMsByWbKeyRs(wbKey).map { it.value }
    }
    override fun getWindowStateByWbKey(wbKey: WorkbookKey): WindowState? {
        return getWindowStateMsByWbKey(wbKey)?.value
    }

    override fun getWindowStateMsById(windowId: String): Ms<WindowState>? {
        return getWindowStateMsByIdRs(windowId).component1()
    }

    override fun getFocusStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowFocusState>?{
        return getFocusStateMsByWbKeyRs(wbKey).component1()
    }
    override fun getWindowStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowState>? {
        return this.getWindowStateMsByWbKeyRs(wbKey).component1()
    }
    override fun getWsStateMsRs(wbwsSt: WbWsSt): Rse<Ms<WorksheetState>> {
        return this.getWsStateMsRs(wbwsSt.wbKeySt,wbwsSt.wsNameSt)
    }

    override fun getWsStateRs(wbwsSt: WbWsSt): Rse<WorksheetState> {
        return getWsStateMsRs(wbwsSt).map { it.value }
    }

    override fun getWsStateMs(wbwsSt: WbWsSt): Ms<WorksheetState>? {
        return getWsStateMsRs(wbwsSt).component1()
    }

    override fun getWsState(wbwsSt: WbWsSt): WorksheetState? {
        return getWsStateMs(wbwsSt)?.value
    }

    override fun getWsStateMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<Ms<WorksheetState>> {
        return this.getWbStateRs(wbKeySt).flatMap {
            it.getWsStateMsRs(wsNameSt)
        }
    }

    override fun getWsStateRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<WorksheetState> {
        return getWsStateMsRs(wbKeySt, wsNameSt).map { it.value }
    }

    override fun getWsStateMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<WorksheetState>? {
        return getWsStateMsRs(wbKeySt, wsNameSt).component1()
    }

    override fun getWsState(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): WorksheetState? {
        return getWsStateMs(wbKeySt, wsNameSt)?.value
    }

    override fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>? {
        return this.getWsStateMs(wbKey, wsName)?.value?.cursorStateMs
    }

    override fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, ErrorReport> {
        return this.getWindowStateMsByWbKeyRs(wbKey).map {
            it.value.focusStateMs
        }
    }

    override fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>> {
        return this.getWbStateRs(wbKey).flatMap { it.getWsStateMsRs(wsName) }
    }

    override fun getWbStateMs(wbKeySt: St<WorkbookKey>): Ms<WorkbookState>? {
        return this.getWbStateMsRs(wbKeySt).component1()
    }

    override fun getWbStateRs(wbKeySt: St<WorkbookKey>): Rse<WorkbookState> {
        return getWbStateMsRs(wbKeySt).map { it.value }
    }

    override fun getWbState(wbKeySt: St<WorkbookKey>): WorkbookState? {
        return getWbStateRs(wbKeySt).component1()
    }

    override fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState> {
        return getWbStateMsRs(wbKey).map { it.value }
    }

    override fun getWbStateMs(wbKey: WorkbookKey): Ms<WorkbookState>? {
        return getWbStateMsRs(wbKey).component1()
    }

    override fun getWbState(wbKey: WorkbookKey): WorkbookState? {
        return getWbStateMs(wbKey)?.value
    }

    override fun getWsStateRs(wbKey: WorkbookKey, wsName: String): Rse<WorksheetState> {
        return getWsStateMsRs(wbKey, wsName).map { it.value }
    }

    override fun getWsStateMs(wbKey: WorkbookKey, wsName: String): Ms<WorksheetState>? {
        return getWsStateMsRs(wbKey, wsName).component1()
    }

    override fun getWsState(wbKey: WorkbookKey, wsName: String): WorksheetState? {
        return getWsStateMs(wbKey, wsName)?.value
    }

    override fun getWsStateMsRs(wbws: WbWs): Rse<Ms<WorksheetState>> {
        return this.getWsStateMsRs(wbws.wbKey, wbws.wsName)
    }

    override fun getWsStateRs(wbws: WbWs): Rse<WorksheetState> {
        return getWsStateMsRs(wbws.wbKey, wbws.wsName).map { it.value }
    }

    override fun getWsStateMs(wbws: WbWs): Ms<WorksheetState>? {
        return getWsStateMsRs(wbws.wbKey, wbws.wsName).component1()
    }

    override fun getWsState(wbws: WbWs): WorksheetState? {
        return getWsStateMs(wbws.wbKey, wbws.wsName)?.value
    }

    override fun getCursorState(wbKey: WorkbookKey, wsName: String): CursorState? {
        return getCursorStateMs(wbKey, wsName)?.value
    }

    override fun getCursorStateMs(wbws: WbWs): Ms<CursorState>? {
        return this.getCursorStateMs(wbws.wbKey, wbws.wsName)
    }

    override fun getCursorState(wbws: WbWs): CursorState? {
        return this.getCursorStateMs(wbws.wbKey, wbws.wsName)?.value
    }

    override fun getCursorState(wbws: WbWsSt): CursorState?{
        return this.getCursorStateMs(wbws)?.value
    }

    override fun getActiveCursorMs(wbKey: WorkbookKey): Ms<CursorState>? {
        val rt = this.getWbState(wbKey)?.let { wbState ->
            wbState.activeSheetState?.let { activeWsState ->
                activeWsState.cursorStateMs
            }
        }
        return rt
    }
}
