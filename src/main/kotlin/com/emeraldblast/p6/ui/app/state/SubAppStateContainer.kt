package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.MutableState
import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import com.emeraldblast.p6.ui.window.focus_state.WindowFocusState
import com.emeraldblast.p6.ui.window.state.WindowState
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map

/**
 * An abstraction layer providing functions for looking up view states that is under app
 */
interface SubAppStateContainer {

    val windowStateMsListMs: Ms<List<Ms<WindowState>>>
    var windowStateMsList: List<MutableState<WindowState>>

    val globalWbStateContMs:Ms<WorkbookStateContainer>
    var globalWbStateCont: WorkbookStateContainer

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

    fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>>
    fun getWsStateRs(wbKey: WorkbookKey, wsName: String): Rse<WorksheetState>
    fun getWsStateMs(wbKey: WorkbookKey, wsName: String): Ms<WorksheetState>?
    fun getWsState(wbKey: WorkbookKey, wsName: String): WorksheetState?

    fun getWsStateMsRs(wbws: WbWs): Rse<Ms<WorksheetState>>
    fun getWsStateRs(wbws: WbWs): Rse<WorksheetState>
    fun getWsStateMs(wbws: WbWs): Ms<WorksheetState>?
    fun getWsState(wbws: WbWs): WorksheetState?

    fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, ErrorReport>
    fun getWindowStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowState>? {
        return this.getWindowStateMsByWbKeyRs(wbKey).component1()
    }

    fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, ErrorReport>
    fun getFocusStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowFocusState>?{
        return getFocusStateMsByWbKeyRs(wbKey).component1()
    }

    fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, ErrorReport>
    fun getWindowStateMsById(windowId: String): Ms<WindowState>?{
        return getWindowStateMsByIdRs(windowId).component1()
    }

    fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>?
    fun getCursorState(wbKey: WorkbookKey, wsName: String): CursorState?
    fun getCursorStateMs(wbws: WbWs): Ms<CursorState>?
    fun getCursorState(wbws: WbWs): CursorState?

    /**
     * get cursor state ms of the active worksheet inside the workbook whose key is [wbKey]
     */
    fun getActiveCursorMs(wbKey: WorkbookKey):Ms<CursorState>?
}
