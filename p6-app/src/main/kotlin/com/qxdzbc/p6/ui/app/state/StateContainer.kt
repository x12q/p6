package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState

/**
 * top-level state container, can access all state + document in the app
 */
interface StateContainer : SubAppStateContainer, DocumentContainer {
    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState: CellEditorState
    val appStateMs:Ms<AppState>
    var appState:AppState

    val centralScriptContainerMs: Ms<CentralScriptContainer>
    var centralScriptContainer: CentralScriptContainer

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
    fun getWindowStateMsDefaultRs(windowId: String?):Rs<Ms<WindowState>, ErrorReport>
}
