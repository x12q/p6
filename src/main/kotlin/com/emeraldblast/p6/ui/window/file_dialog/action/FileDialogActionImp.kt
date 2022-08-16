package com.emeraldblast.p6.ui.window.file_dialog.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.window.file_dialog.state.FileDialogState

class FileDialogActionImp(
    private val dialogStateMs:Ms<FileDialogState>
) : FileDialogAction {
    private var ds by dialogStateMs
}
