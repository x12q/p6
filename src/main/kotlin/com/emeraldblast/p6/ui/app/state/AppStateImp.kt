package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.document.script.ScriptContainer
import com.emeraldblast.p6.app.document.script.ScriptContainerImp
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.oddity.OddityContainer
import com.emeraldblast.p6.app.oddity.OddityContainerImp
import com.emeraldblast.p6.di.False
import com.emeraldblast.p6.di.state.app_state.*
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.app.ActiveWindowPointer
import com.emeraldblast.p6.ui.app.ActiveWindowPointerImp
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.script_editor.code_container.CentralScriptContainer
import com.emeraldblast.p6.ui.script_editor.state.CodeEditorState
import com.emeraldblast.p6.ui.window.state.WindowState
import com.emeraldblast.p6.ui.window.state.WindowStateFactory
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.unwrapError
import javax.inject.Inject

data class AppStateImp @Inject constructor(
    @False
    override val codeEditorIsOpen: Boolean,
    @AppOddityContMs
    override val oddityContainerMs: Ms<OddityContainer> = ms(OddityContainerImp()),
    @WindowActivePointerMs
    override val activeWindowPointerMs: Ms<ActiveWindowPointer> = ms(ActiveWindowPointerImp(null)),
    @AppScriptContMs
    val appScriptContainerMs: Ms<ScriptContainer> = ms(ScriptContainerImp()),
    @CentralScriptContMs
    override val centralScriptContainerMs: Ms<CentralScriptContainer>,
    @CodeEditorStateMs
    override val codeEditorStateMs: Ms<CodeEditorState>,
    override val windowStateFactory: WindowStateFactory,
    private val wbStateFactory: WorkbookStateFactory,
    @StateContainerMs
    override val stateContMs: Ms<StateContainer>,
    @DocumentContainerMs
    override val documentContainerMs: Ms<DocumentContainer>,
    @TranslatorContainerMs
    override val translatorContainerMs: Ms<TranslatorContainer>,
    @CellEditorStateMs
    override val cellEditorStateMs: Ms<CellEditorState>,
) : AppState,
    StateContainer by stateContMs.value,
    DocumentContainer by documentContainerMs.value {
    override var translatorContainer: TranslatorContainer by translatorContainerMs
    override var documentContainer by documentContainerMs
    override var cellEditorState: CellEditorState by cellEditorStateMs

    override var stateCont by stateContMs

    override var activeWindowPointer: ActiveWindowPointer by activeWindowPointerMs
    override val activeWindowStateMs: Ms<WindowState>?
        get() = activeWindowPointer.windowId?.let{this.getWindowStateMsById(it)}
    override var activeWindowState: WindowState?
        get() = activeWindowStateMs?.value
        set(value) {}

    override var globalWbCont: WorkbookContainer by globalWbContMs

    override var oddityContainer: OddityContainer by oddityContainerMs

    override var globalWbStateCont: WorkbookStateContainer by globalWbStateContMs

    override var centralScriptContainer: CentralScriptContainer by centralScriptContainerMs

    override var codeEditorState: CodeEditorState by codeEditorStateMs

    override fun openCodeEditor(): AppState {
        return this.copy(codeEditorIsOpen = true)
    }

    override fun closeCodeEditor(): AppState {
        return this.copy(codeEditorIsOpen = false)
    }

    override fun replaceWb(newWb: Workbook): AppState {
        documentContainer = documentContainer.replaceWb(newWb)
        return this
    }

    override fun addWbStateFor(wb: Workbook): AppState {
        stateCont = stateCont.addWbStateFor(wb)
        return this
    }

    override fun removeWindowState(windowState: Ms<WindowState>): AppState {
        stateCont = stateCont.removeWindowState(windowState)
        return this
    }

    override fun removeWindowState(windowId: String): AppState {
        stateCont = stateCont.removeWindowState(windowId)
        return this
    }

    override fun createNewWindowStateMs(): Pair<AppState, Ms<WindowState>> {
        val p = stateCont.createNewWindowStateMs()
        stateCont = p.first
        return Pair(this, p.second)
    }

    override fun createNewWindowStateMs(windowId: String): Pair<AppState, Ms<WindowState>> {
        val p = stateCont.createNewWindowStateMs(windowId)
        stateCont = p.first
        return Pair(this, p.second)
    }

    override fun addWindowState(windowState: Ms<WindowState>): AppState {
        stateCont = stateCont.addWindowState(windowState)
        return this
    }

    override fun queryStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult {
        val windowStateMsRs = this.getWindowStateMsByWbKeyRs(workbookKey)
        if (windowStateMsRs is Ok) {
            val windowstateMs = windowStateMsRs.value
            val workbookStateMsRs = globalWbStateCont.getWbStateMsRs(workbookKey)

            if (workbookStateMsRs is Ok) {
                return QueryByWorkbookKeyResult(
                    windowStateOrNull = windowstateMs,
                    workbookStateMsOrNull = workbookStateMsRs.value,
                    oddityContainerMs = windowstateMs.value.oddityContainerMs
                )
            } else {
                return QueryByWorkbookKeyResult(
                    _errorReport = workbookStateMsRs.unwrapError(),
                    oddityContainerMs = windowstateMs.value.oddityContainerMs
                )
            }
        } else {
            return QueryByWorkbookKeyResult(
                _errorReport = windowStateMsRs.unwrapError(),
                oddityContainerMs = this.oddityContainerMs
            )
        }
    }

}
