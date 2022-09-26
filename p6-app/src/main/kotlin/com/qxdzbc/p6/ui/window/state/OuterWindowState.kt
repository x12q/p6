package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.window.close_window.WithWindowId
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState

/**
 * The purpose of this wrapping layer is to prevent the forced-push-to-top effect when updating app, window, wb states
 */
interface OuterWindowState : WithWindowId {
    val innerWindowStateMs: Ms<WindowState>
    var innerWindowState: WindowState
    var focusState: WindowFocusState
    val errorContainer: ErrorContainer
    val windowTitle: String
    val openCommonFileDialog: Boolean
    var loadDialogState: FileDialogState
    val saveDialogState: FileDialogState
    val activeWbKey: WorkbookKey?
}
