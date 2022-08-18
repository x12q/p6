package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.MutableState
import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import com.emeraldblast.p6.ui.window.focus_state.WindowFocusState
import com.emeraldblast.p6.ui.window.state.WindowState
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map

/**
 * An abstraction layer providing functions for looking up view states
 */
interface StateContainer {

    val windowStateMsListMs: Ms<List<Ms<WindowState>>>
    var windowStateMsList: List<MutableState<WindowState>>

    val globalWbStateContMs:Ms<WorkbookStateContainer>
    var globalWbStateCont: WorkbookStateContainer

    fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2>
    /**
     * create and add a new wb state for [wb] if it yet to have a state of its own
     */
    fun addWbStateFor(wb: Workbook):StateContainer

    fun removeWindowState(windowState: Ms<WindowState>):StateContainer
    fun removeWindowState(windowId:String):StateContainer
    fun addWindowState(windowState: Ms<WindowState>):StateContainer
    fun createNewWindowStateMs(): Pair<StateContainer,Ms<WindowState>>
    fun createNewWindowStateMs(windowId: String): Pair<StateContainer,Ms<WindowState>>

    fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>>

    fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState> {
        return getWbStateMsRs(wbKey).map { it.value }
    }

    fun getWbStateMs(wbKey: WorkbookKey): Ms<WorkbookState>? {
        return getWbStateMsRs(wbKey).component1()
    }

    fun getWbState(wbKey: WorkbookKey): WorkbookState? {
        return getWbStateMs(wbKey)?.value
    }

    fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>>

    fun getWsStateRs(wbKey: WorkbookKey, wsName: String): Rse<WorksheetState> {
        return getWsStateMsRs(wbKey, wsName).map { it.value }
    }

    fun getWsStateMs(wbKey: WorkbookKey, wsName: String): Ms<WorksheetState>? {
        return getWsStateMsRs(wbKey, wsName).component1()
    }

    fun getWsState(wbKey: WorkbookKey, wsName: String): WorksheetState? {
        return getWsStateMs(wbKey, wsName)?.value
    }

    fun getWsStateMsRs(wbws: WbWs): Rse<Ms<WorksheetState>> {
        return this.getWsStateMsRs(wbws.wbKey, wbws.wsName)
    }

    fun getWsStateRs(wbws: WbWs): Rse<WorksheetState> {
        return getWsStateMsRs(wbws.wbKey, wbws.wsName).map { it.value }
    }

    fun getWsStateMs(wbws: WbWs): Ms<WorksheetState>? {
        return getWsStateMsRs(wbws.wbKey, wbws.wsName).component1()
    }

    fun getWsState(wbws: WbWs): WorksheetState? {
        return getWsStateMs(wbws.wbKey, wbws.wsName)?.value
    }

    fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, ErrorReport>
    fun getWindowStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowState>? {
        return this.getWindowStateMsByWbKeyRs(wbKey).component1()
    }

    fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>,ErrorReport>
    fun getFocusStateMsByWbKey(wbKey: WorkbookKey): Ms<WindowFocusState>?{
        return getFocusStateMsByWbKeyRs(wbKey).component1()
    }

    fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>,ErrorReport>
    fun getWindowStateMsById(windowId: String): Ms<WindowState>?{
        return getWindowStateMsByIdRs(windowId).component1()
    }

    fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>?
    fun getCursorState(wbKey: WorkbookKey, wsName: String): CursorState? {
        return getCursorStateMs(wbKey, wsName)?.value
    }
    fun getCursorStateMs(wbws: WbWs): Ms<CursorState>?{
        return this.getCursorStateMs(wbws.wbKey,wbws.wsName)
    }
    fun getCursorState(wbws: WbWs): CursorState? {
        return this.getCursorStateMs(wbws.wbKey,wbws.wsName)?.value
    }

    /**
     * get cursor state ms of the active worksheet inside the workbook whose key is [wbKey]
     */
    fun getActiveCursorMs(wbKey: WorkbookKey):Ms<CursorState>?{
        val rt=this.getWbState(wbKey)?.let { wbState->
            wbState.activeSheetState?.let {activeWsState->
                activeWsState.cursorStateMs
            }
        }
        return rt
    }
}
