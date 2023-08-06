package com.qxdzbc.p6.ui.window.di.comp

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.app.err.ErrorContainerImp
import com.qxdzbc.p6.ui.window.di.qualifiers.LoadDialogStateMs
import com.qxdzbc.p6.ui.window.di.qualifiers.SaveDialogStateMs
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogStateImp
import com.qxdzbc.p6.ui.window.state.*
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarState
import com.qxdzbc.p6.ui.window.tool_bar.state.ToolBarStateImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CompletableDeferred
import java.nio.file.Path

@Module
interface WindowModule {

    @Binds
    @WindowScope
    fun windowState(i: WindowStateImp):WindowState

    companion object {

        @Provides
        @WindowScope
        fun activeWbPointerMs(): Ms<ActiveWorkbookPointer> = ms(ActiveWorkbookPointerImp(null))

        @Provides
        @WindowScope
        fun wbKeyMsSetMs(): Ms<Set<Ms<WorkbookKey>>> = ms(emptySet())

        @Provides
        @WindowScope
        fun commonFileDialogJobMs(): Ms<CompletableDeferred<Path?>?> = ms(null)

        @Provides
        @WindowScope
        fun toolBarStateMs(): Ms<ToolBarState> = ms(ToolBarStateImp())

        @Provides
        @WindowScope
        fun errorContainerMs(): Ms<ErrorContainer> = ms(ErrorContainerImp())

        @Provides
        @WindowScope
        @SaveDialogStateMs
        fun saveDialogStateMs(): Ms<FileDialogState> = ms(FileDialogStateImp())

        @Provides
        @WindowScope
        @LoadDialogStateMs
        fun loadDialogStateMs(): Ms<FileDialogState> = ms(FileDialogStateImp())

    }
}