package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.state.WindowState

/**
 * A fixed point in the app, holding all the state.
 */
interface AppState {
    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState: CellEditorState

    val subAppStateCont: SubAppStateContainer

    val activeWindowPointerMs: Ms<ActiveWindowPointer>
    var activeWindowPointer: ActiveWindowPointer

    val activeWindowStateMs: Ms<WindowState>?
    val activeWindowState: WindowState?

    val errorContainerMs: MutableState<ErrorContainer>
    var errorContainer: ErrorContainer

    /**
     * Extract information related to a workbook key. Such as the workbook the key is pointing to, the window in which the workbook locates.
     */
    @Deprecated("dont use, pending to be deleted")
    fun queryStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult

    val documentContainerMs: Ms<DocumentContainer>
    var documentContainer: DocumentContainer

    var translatorContainer: TranslatorContainer
    val translatorContMs: Ms<TranslatorContainer>
}


