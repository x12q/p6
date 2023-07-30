package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms

/**
 * The root of all the state objects in the app. Although all state objects can be accessed via the inject graph. This interface act as a formal and referencable root.
 */
interface AppState {
    /**
     * There's only one cell editor for the entire app.
     */
    val cellEditorStateMs:Ms<CellEditorState>
    var cellEditorState: CellEditorState

    val stateCont: StateContainer
    val documentContainer: DocumentContainer
    val translatorContainer: TranslatorContainer

    /**
     * Error container at the app level. This is the fall-back error container when no other error container (workbook, worksheet, etc.) can handle an error.
     */
    val appErrorContainerMs: Ms<ErrorContainer>
    var appErrorContainer: ErrorContainer
}


