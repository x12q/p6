package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.window.state.WindowState

/**
 * top-level state container, can access all state + document in the app
 */
interface StateContainer : SubAppStateContainer, DocumentContainer {
    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState:CellEditorState
    val appStateMs:Ms<AppState>
    var appState:AppState

    val centralScriptContainerMs: Ms<CentralScriptContainer>
    var centralScriptContainer: CentralScriptContainer

    override fun createNewWindowStateMs(): Pair<StateContainer, Ms<WindowState>>
    override fun createNewWindowStateMs(windowId: String): Pair<StateContainer, Ms<WindowState>>
    override fun addWbStateFor(wb: Workbook): StateContainer
    override fun removeWindowState(windowState: Ms<WindowState>): StateContainer
    override fun removeWindowState(windowId: String): StateContainer
    override fun addWindowState(windowState: Ms<WindowState>): StateContainer

}
