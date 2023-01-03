package com.qxdzbc.p6.ui.window.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.ui.window.dialog.WindowDialogHostState
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class OuterWindowStateImp @AssistedInject constructor(
    @Assisted override val innerWindowStateMs: Ms<WindowState>
) : OuterWindowState {
    override val dialogHostState: WindowDialogHostState
        get() = innerWindowState.dialogHostState
    override var innerWindowState: WindowState by innerWindowStateMs
    override val windowId: String get() = innerWindowState.id
    override var focusState: WindowFocusState by innerWindowState.focusStateMs
    override val errorContainer: ErrorContainer by innerWindowState.errorContainerMs
    override val activeWbKey get() = this.innerWindowState.activeWbKey
    override val activeWbKeyMs: Ms<WorkbookKey>?
        get() = this.innerWindowState.activeWbPointer.wbKeyMs
    override val activeWbPointer: ActiveWorkbookPointer
        get() = this.innerWindowState.activeWbPointer


    override val windowTitle: String
        get() = this.innerWindowState.windowTitle

    override val openCommonFileDialog: Boolean
        get() = innerWindowState.openCommonFileDialog

    override var loadDialogState: FileDialogState by innerWindowState.loadDialogStateMs

    override val saveDialogState: FileDialogState
        get() = innerWindowState.saveDialogState
}
