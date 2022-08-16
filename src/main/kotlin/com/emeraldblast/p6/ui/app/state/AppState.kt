package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.MutableState
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.oddity.OddityContainer
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.app.ActiveWindowPointer
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.script_editor.code_container.CentralScriptContainer
import com.emeraldblast.p6.ui.script_editor.state.CodeEditorState
import com.emeraldblast.p6.ui.window.state.WindowState
import com.emeraldblast.p6.ui.window.state.WindowStateFactory

/**
 * A fixed point in the app, holding all the state
 */
interface AppState :DocumentContainer,StateContainer{

    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState: CellEditorState

    @Deprecated("don't use, use translator cont directly")
    fun getTranslator(wbKey: WorkbookKey, wsName: String): P6Translator<ExUnit>

    val stateContMs:Ms<StateContainer>
    var stateCont: StateContainer
    val windowStateFactory:WindowStateFactory

    val centralScriptContainerMs:Ms<CentralScriptContainer>
    var centralScriptContainer:CentralScriptContainer

    val codeEditorStateMs:Ms<CodeEditorState>
    var codeEditorState: CodeEditorState
    val codeEditorIsOpen:Boolean
    fun openCodeEditor():AppState
    fun closeCodeEditor():AppState

    val activeWindowPointerMs:Ms<ActiveWindowPointer>
    var activeWindowPointer: ActiveWindowPointer

    val activeWindowStateMs: Ms<WindowState>?
    var activeWindowState: WindowState?

    val oddityContainerMs: MutableState<OddityContainer>
    var oddityContainer:OddityContainer

    /**
     * Extract information related to a workbook key. Such as the workbook the key is pointing to, the window in which the workbook locates.
     */
    fun queryStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult


    override fun replaceWb(newWb:Workbook):AppState

    override fun addWbStateFor(wb: Workbook): AppState
    override fun addWbStateFor(wbKey: WorkbookKey): StateContainer
    override fun removeWindowState(windowState: Ms<WindowState>): AppState
    override fun removeWindowState(windowId: String): AppState
    override fun createNewWindowStateMs(): Pair<AppState, Ms<WindowState>>
    override fun createNewWindowStateMs(windowId: String): Pair<AppState, Ms<WindowState>>
    override fun addWindowState(windowState: Ms<WindowState>): AppState

    val documentContainerMs: Ms<DocumentContainer>
    var documentContainer: DocumentContainer
    var translatorContainer: TranslatorContainer
    val translatorContainerMs: Ms<TranslatorContainer>
}


