package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.MutableState
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.github.michaelbull.result.Result

/**
 * An abstraction layer providing functions for looking up view states that is under app
 */
interface SubAppStateContainer {
    val windowStateMsListMs: Ms<List<Ms<WindowState>>>
    var windowStateMsList: List<MutableState<WindowState>>

    val wbStateContMs:Ms<WorkbookStateContainer>
    var wbStateCont: WorkbookStateContainer

    fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2>
    /**
     * create and add a new wb state for [wb] if it yet to have a state of its own
     */
    fun addWbStateFor(wb: Workbook):SubAppStateContainer

    fun removeWindowState(windowState: Ms<WindowState>):SubAppStateContainer
    fun removeWindowState(windowId:String):SubAppStateContainer
    fun addWindowState(windowState: Ms<WindowState>):SubAppStateContainer
    fun createNewWindowStateMs(): Pair<SubAppStateContainer,Ms<WindowState>>
    fun createNewWindowStateMs(windowId: String): Pair<SubAppStateContainer,Ms<WindowState>>

    fun getWbStateMsRs(wbKeySt:St<WorkbookKey>): Rse<Ms<WorkbookState>>
    fun getWbStateMs(wbKeySt:St<WorkbookKey>):Ms<WorkbookState>?
    fun getWbStateRs(wbKeySt:St<WorkbookKey>): Rse<WorkbookState>
    fun getWbState(wbKeySt:St<WorkbookKey>):WorkbookState?

    fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>>
    fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState>
    fun getWbStateMs(wbKey: WorkbookKey): Ms<WorkbookState>?
    fun getWbState(wbKey: WorkbookKey): WorkbookState?

    fun getWsStateMsRs(wbKeySt:St <WorkbookKey>, wsNameSt:St<String> ): Rse<Ms<WorksheetState>>
    fun getWsStateRs(wbKeySt:St <WorkbookKey>, wsNameSt:St<String> ): Rse<WorksheetState>
    fun getWsStateMs(wbKeySt:St <WorkbookKey>, wsNameSt:St<String> ): Ms<WorksheetState>?
    fun getWsState(wbKeySt:St <WorkbookKey>, wsNameSt:St<String> ): WorksheetState?

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

    fun getWindowStateByIdRs(windowId:String):Rse<WindowState>
    fun getWindowStateById(windowId:String):WindowState?
    fun getWindowStateByWbKeyRs(wbKey: WorkbookKey): Rse<WindowState>
    fun getWindowStateByWbKey(wbKey: WorkbookKey):WindowState?

    fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, ErrorReport>
    fun getWindowStateMsById(windowId: String): Ms<WindowState>?

    fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>?
    fun getCursorState(wbKey: WorkbookKey, wsName: String): CursorState?
    fun getCursorStateMs(wbws: WbWs): Ms<CursorState>?
    fun getCursorState(wbws: WbWs): CursorState?

    /**
     * get cursor state ms of the active worksheet inside the workbook whose key is [wbKey]
     */
    fun getActiveCursorMs(wbKey: WorkbookKey):Ms<CursorState>?

}
