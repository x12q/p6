package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState

/**
 * top-level state container, can access all state + document in the app
 */
interface StateContainer : SubAppStateContainer, DocumentContainer {
    fun getActiveWindowStateMs():Ms<WindowState>?
    fun getActiveWindowState():WindowState?

    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState: CellEditorState
    val appState:AppState

    fun getActiveWorkbook(): Workbook?
    fun getActiveWorkbookRs(): Rse<Workbook>

    override fun createNewWindowStateMs(): Pair<StateContainer, Ms<OuterWindowState>>
    override fun createNewWindowStateMs(windowId: String): Pair<StateContainer, Ms<OuterWindowState>>
    override fun addWbStateFor(wb: Workbook): StateContainer
    override fun removeWindowState(windowState: Ms<WindowState>): StateContainer
    override fun removeWindowState(windowId: String): StateContainer
    override fun addWindowState(windowState: Ms<WindowState>): StateContainer
    /**
     * get window state respective to [windowId],
     * if [windowId] is null, get the active window, or the first, or create a new window
     */
    fun getWindowStateMsDefaultRs(windowId: String?):Rse<Ms<WindowState>>

    /**
     * get cursor state ms of the active worksheet inside the active workbook
     */
    fun getActiveCursorStateMs(): Ms<CursorState>?
    fun getActiveCursorState(): CursorState?

    fun getActiveWbStateMs(): Ms<WorkbookState>?
    fun getActiveWbState(): WorkbookState?
}
