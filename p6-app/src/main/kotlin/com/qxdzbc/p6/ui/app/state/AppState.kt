package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.qxdzbc.p6.ui.window.state.OuterWindowState
import com.qxdzbc.p6.ui.window.state.WindowState

/**
 * A fixed point in the app, holding all the state
 */
interface AppState : DocumentContainer, SubAppStateContainer {
    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState: CellEditorState

    val subAppStateContMs: Ms<SubAppStateContainer>
    var subAppStateCont: SubAppStateContainer

    val centralScriptContainerMs: Ms<CentralScriptContainer>
    var centralScriptContainer: CentralScriptContainer

    val codeEditorStateMs: Ms<CodeEditorState>
    var codeEditorState: CodeEditorState
    val codeEditorIsOpen: Boolean
    fun openCodeEditor(): AppState
    fun closeCodeEditor(): AppState

    val activeWindowPointerMs: Ms<ActiveWindowPointer>
    var activeWindowPointer: ActiveWindowPointer

    val activeWindowStateMs: Ms<WindowState>?
    val activeWindowState: WindowState?

    val errorContainerMs: MutableState<ErrorContainer>
    var errorContainer: ErrorContainer

    /**
     * Extract information related to a workbook key. Such as the workbook the key is pointing to, the window in which the workbook locates.
     */
    fun queryStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult

    override fun replaceWb(newWb: Workbook): AppState

    override fun addWbStateFor(wb: Workbook): AppState
    override fun removeWindowState(windowState: Ms<WindowState>): AppState
    override fun removeWindowState(windowId: String): AppState
    override fun createNewWindowStateMs(): Pair<AppState, Ms<OuterWindowState>>
    override fun createNewWindowStateMs(windowId: String): Pair<AppState, Ms<OuterWindowState>>
    override fun addWindowState(windowState: Ms<WindowState>): AppState

    val docContMs: Ms<DocumentContainer>
    var docCont: DocumentContainer
    var translatorContainer: TranslatorContainer
    val translatorContMs: Ms<TranslatorContainer>
}


