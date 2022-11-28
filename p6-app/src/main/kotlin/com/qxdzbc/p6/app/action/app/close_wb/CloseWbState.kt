package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState
import com.qxdzbc.p6.ui.window.state.WindowState

data class CloseWbState(
    val wbCont: WorkbookContainer,
    val codeEditorState: CodeEditorState,
    val translatorContainer: TranslatorContainer,
    val centralScriptContainer: CentralScriptContainer,
    val respectiveWindowState: WindowState?,
)
