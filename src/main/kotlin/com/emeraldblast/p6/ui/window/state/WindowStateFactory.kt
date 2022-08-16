package com.emeraldblast.p6.ui.window.state

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.oddity.OddityContainer
import com.emeraldblast.p6.app.oddity.OddityContainerImp
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.window.file_dialog.state.FileDialogState
import com.emeraldblast.p6.ui.window.file_dialog.state.FileDialogStateImp
import com.emeraldblast.p6.ui.window.kernel_dialog.ShowDialogState
import com.emeraldblast.p6.ui.window.kernel_dialog.ShowDialogStateImp
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
        oddityContainerMs: Ms<OddityContainer> = ms(OddityContainerImp()),
        @Assisted("saveDialogStateMs")
        saveDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
        @Assisted("loadDialogStateMs")
        loadDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
        id: String = UUID.randomUUID().toString(),
        wbKeySet: Set<WorkbookKey> = emptySet(),
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
            wbKeys: Collection<WorkbookKey> = emptySet(),
            id: String = UUID.randomUUID().toString()
        ):WindowStateImp{
            val activeWbPointerMs: Ms<ActiveWorkbookPointer> = ms(ActiveWorkbookPointerImp(wbKeys.firstOrNull()))
            return this.create(
                activeWorkbookPointerMs = activeWbPointerMs,
                id=id,
                wbKeySet = wbKeys.toSet()
            )
        }
    }
}
