package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.app.err.ErrorContainerImp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogStateImp
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogState
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogStateImp
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarStateImp
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
        toolBarStateMs: Ms<ToolBarState> = ms(ToolBarStateImp()),
        errorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
        @Assisted("saveDialogStateMs")
        saveDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
        @Assisted("loadDialogStateMs")
        loadDialogStateMs: Ms<FileDialogState> = ms(FileDialogStateImp()),
        id: String = UUID.randomUUID().toString(),
        wbKeyMsSetMs: Ms<Set<Ms<WorkbookKey>>> = ms(emptySet()),
        @Assisted("showStartKernelDialogStateMs")
        showStartKernelDialogStateMs: Ms<ShowDialogState> = ms(ShowDialogStateImp()),
        @Assisted("showConnectToKernelDialogStateMs")
        showConnectToKernelDialogStateMs: Ms<ShowDialogState> = ms(ShowDialogStateImp()),
        commonFileDialogJobMs: Ms<CompletableDeferred<Path?>?> = ms(null),
//        dialogHostStateMs: Ms<WindowDialogGroupState> = ms(WindowDialogGroupStateImp())
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
            return create(
                activeWorkbookPointerMs = activeWbPointerMs,
                id=id,
                wbKeyMsSetMs = ms(wbKeys.toSet())
            )
        }
    }
}
