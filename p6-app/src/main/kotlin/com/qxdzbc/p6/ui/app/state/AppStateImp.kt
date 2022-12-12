package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.app.oddity.ErrorContainerImp
import com.qxdzbc.p6.di.False
import com.qxdzbc.p6.di.state.app_state.*
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.ActiveWindowPointerImp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.unwrapError
import javax.inject.Inject

data class AppStateImp @Inject constructor(
    @AppErrorContMs
    override val errorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
    override val activeWindowPointerMs: Ms<ActiveWindowPointer> = ms(ActiveWindowPointerImp(null)),
    val windowStateFactory: WindowStateFactory,
    private val wbStateFactory: WorkbookStateFactory,
    override val subAppStateContMs: Ms<SubAppStateContainer>,
    override val docContMs: Ms<DocumentContainer>,
    override val translatorContMs: Ms<TranslatorContainer>,
    override val cellEditorStateMs: Ms<CellEditorState>,
) : AppState {

    override var docCont by docContMs
    override var subAppStateCont by subAppStateContMs
    override var translatorContainer: TranslatorContainer by translatorContMs
    override var cellEditorState: CellEditorState by cellEditorStateMs
    override var errorContainer: ErrorContainer by errorContainerMs
    override var activeWindowPointer: ActiveWindowPointer by activeWindowPointerMs
    override val activeWindowStateMs: Ms<WindowState>?
        get() = activeWindowPointer.windowId?.let{
            this.subAppStateCont.getWindowStateMsById(it)
        }
    override val activeWindowState: WindowState?
        get() = activeWindowStateMs?.value

    override fun queryStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult {
        val windowStateMsRs = this.subAppStateCont.getWindowStateMsByWbKeyRs(workbookKey)
        if (windowStateMsRs is Ok) {
            val oWindowstateMs = windowStateMsRs.value
            val workbookStateMsRs = subAppStateCont.getWbStateMsRs(workbookKey)
            val windowstateMs = oWindowstateMs
            if (workbookStateMsRs is Ok) {
                return QueryByWorkbookKeyResult(
                    windowStateOrNull = windowstateMs,
                    workbookStateMsOrNull = workbookStateMsRs.value,
                    errorContainerMs = windowstateMs.value.errorContainerMs
                )
            } else {
                return QueryByWorkbookKeyResult(
                    _errorReport = workbookStateMsRs.unwrapError(),
                    errorContainerMs = windowstateMs.value.errorContainerMs
                )
            }
        } else {
            return QueryByWorkbookKeyResult(
                _errorReport = windowStateMsRs.unwrapError(),
                errorContainerMs = this.errorContainerMs
            )
        }
    }
}
