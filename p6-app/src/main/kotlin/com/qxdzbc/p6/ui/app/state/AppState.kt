package com.qxdzbc.p6.ui.app.state

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

    val stateCont: StateContainer

    val activeWindowPointer: ActiveWindowPointer

    val activeWindowStateMs: Ms<WindowState>?
    val activeWindowState: WindowState?

    val errorContainerMs: Ms<ErrorContainer>
    var errorContainer: ErrorContainer

    val documentContainer: DocumentContainer
    val translatorContainer: TranslatorContainer
}


