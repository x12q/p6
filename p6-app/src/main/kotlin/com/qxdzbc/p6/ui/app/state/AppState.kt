package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
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

    /**
     * Ms is justified here. But is it easy to use?
     * So any one who use this pointer will need to obtain a Ms, not the pointer itself.
     */
    val activeWindowPointer: ActiveWindowPointer

    val activeWindowStateMs: Ms<WindowState>?
    val activeWindowState: WindowState?

    val errorContainerMs: Ms<ErrorContainer>
    var errorContainer: ErrorContainer

    /**
     * Extract information related to a workbook key. Such as the workbook the key is pointing to, the window in which the workbook locates.
     */
    @Deprecated("dont use, pending to be deleted")
    fun queryStateByWorkbookKey(workbookKey: WorkbookKey): QueryByWorkbookKeyResult

    val documentContainer: DocumentContainer
    val translatorContainer: TranslatorContainer
}


