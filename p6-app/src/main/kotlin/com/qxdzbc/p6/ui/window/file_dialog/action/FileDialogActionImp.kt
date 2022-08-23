package com.qxdzbc.p6.ui.window.file_dialog.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState

class FileDialogActionImp(
    private val dialogStateMs:Ms<FileDialogState>
) : FileDialogAction {
    private var ds by dialogStateMs
}
