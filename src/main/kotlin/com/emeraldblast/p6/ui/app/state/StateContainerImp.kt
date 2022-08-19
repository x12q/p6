package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import com.emeraldblast.p6.ui.window.focus_state.WindowFocusState
import com.emeraldblast.p6.ui.window.state.WindowState
import com.github.michaelbull.result.Result
import javax.inject.Inject

class StateContainerImp @Inject constructor(
    override val appStateMs: Ms<AppState>
) : StateContainer {
    override var appState by appStateMs

    override val cellEditorStateMs: Ms<CellEditorState>
        get() = appState.cellEditorStateMs
    override var cellEditorState: CellEditorState by cellEditorStateMs

    override val windowStateMsListMs: Ms<List<Ms<WindowState>>>
        get() = appState.windowStateMsListMs
    override var windowStateMsList: List<MutableState<WindowState>> by windowStateMsListMs

    override val globalWbStateContMs: Ms<WorkbookStateContainer>
        get() = appState.globalWbStateContMs
    override var globalWbStateCont: WorkbookStateContainer by globalWbStateContMs

    override fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2> {
        return appState.getStateByWorkbookKeyRs(workbookKey)
    }

    override fun addWbStateFor(wb: Workbook): StateContainer {
        appState = appState.addWbStateFor(wb)
        return this
    }

    override fun removeWindowState(windowState: Ms<WindowState>): StateContainer {
        appState = appState.removeWindowState(windowState)
        return this
    }

    override fun removeWindowState(windowId: String): StateContainer {
        appState= appState.removeWindowState(windowId)
        return this
    }

    override fun addWindowState(windowState: Ms<WindowState>): StateContainer {
        appState= appState.addWindowState(windowState)
        return this
    }

    override fun createNewWindowStateMs(): Pair<StateContainer, Ms<WindowState>> {
         val o=appState.createNewWindowStateMs()
        appState= o.first
        return this to o.second
    }

    override fun createNewWindowStateMs(windowId: String): Pair<StateContainer, Ms<WindowState>> {
        val o = appState.createNewWindowStateMs(windowId)
        appState = o.first
        return this to o.second
    }

    override fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>> {
        return appState.getWbStateMsRs(wbKey)
    }

    override fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>> {
        return appState.getWsStateMsRs(wbKey, wsName)
    }

    override fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, ErrorReport> {
        return appState.getWindowStateMsByWbKeyRs(wbKey)
    }

    override fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, ErrorReport> {
        return appState.getFocusStateMsByWbKeyRs(wbKey)
    }

    override fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, ErrorReport> {
        return appState.getWindowStateMsByIdRs(windowId)
    }

    override fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>? {
        return appState.getCursorStateMs(wbKey, wsName)
    }
}
