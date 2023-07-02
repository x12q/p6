package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.app.err.ErrorContainerImp
import com.qxdzbc.p6.di.state.app_state.*
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.ActiveWindowPointerImp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.unwrapError
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(P6AnvilScope::class)
data class AppStateImp @Inject constructor(
    @AppErrorContMs
    override val errorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
    override val activeWindowPointer: ActiveWindowPointer = ActiveWindowPointerImp(),
    override var subAppStateCont: SubAppStateContainer,
    override var documentContainer: DocumentContainer,
    override val translatorContMs: Ms<TranslatorContainer>,
    override val cellEditorStateMs: Ms<CellEditorState>,
) : AppState {

    override var translatorContainer: TranslatorContainer by translatorContMs
    override var cellEditorState: CellEditorState by cellEditorStateMs
    override var errorContainer: ErrorContainer by errorContainerMs

    override val activeWindowStateMs: Ms<WindowState>?
        get() = activeWindowPointer.windowId?.let {
            this.subAppStateCont.getWindowStateMsById(it)
        }
    override val activeWindowState: WindowState?
        get() = activeWindowStateMs?.value

    @Deprecated("dont use, pending to be deleted")
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
