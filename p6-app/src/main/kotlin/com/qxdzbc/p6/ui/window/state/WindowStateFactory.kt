package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.ErrorContainer
import com.qxdzbc.p6.app.oddity.ErrorContainerImp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogStateImp
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogState
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogStateImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.CompletableDeferred
import java.nio.file.Path
import java.util.*

@AssistedFactory
interface WindowStateFactory {
    fun create(
        activeWorkbookPointerMs: Ms<ActiveWorkbookPointer>,
        // ==================================================== //
        errorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
        @Assisted("saveDialogStateMs")
        saveDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
        @Assisted("loadDialogStateMs")
        loadDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
        id: String = UUID.randomUUID().toString(),
        wbKeyMsSet: Set<Ms<WorkbookKey>> = emptySet(),
        @Assisted("showStartKernelDialogStateMs")
        showStartKernelDialogStateMs: Ms<ShowDialogState> = ms(ShowDialogStateImp()),
        @Assisted("showConnectToKernelDialogStateMs")
        showConnectToKernelDialogStateMs: Ms<ShowDialogState> = ms(ShowDialogStateImp()),
        commonFileDialogJob: CompletableDeferred<Path?>? = null,
    ): WindowStateImp

    companion object {
        /**
         * create an empty window state
         */
        fun WindowStateFactory.createDefault(
            wbKeys: Collection<Ms<WorkbookKey>> = emptySet(),
            id: String = UUID.randomUUID().toString()
        ):WindowStateImp{
            val activeWbPointerMs: Ms<ActiveWorkbookPointer> = ms(ActiveWorkbookPointerImp(wbKeys.firstOrNull()))
            return this.create(
                activeWorkbookPointerMs = activeWbPointerMs,
                id=id,
                wbKeyMsSet = wbKeys.toSet()
            )
        }
    }
}
