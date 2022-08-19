package com.emeraldblast.p6.ui.app.state

import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.window.state.WindowState

/**
 * top-level state container, can access all state in the app
 */
interface StateContainer : SubAppStateContainer {
    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState:CellEditorState
    val appStateMs:Ms<AppState>
    var appState:AppState
    override fun createNewWindowStateMs(): Pair<StateContainer, Ms<WindowState>>
    override fun createNewWindowStateMs(windowId: String): Pair<StateContainer, Ms<WindowState>>
    override fun addWbStateFor(wb: Workbook): StateContainer
    override fun removeWindowState(windowState: Ms<WindowState>): StateContainer
    override fun removeWindowState(windowId: String): StateContainer
    override fun addWindowState(windowState: Ms<WindowState>): StateContainer

}


