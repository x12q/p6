package com.qxdzbc.p6.ui.window.file_dialog.action

import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogStateImp
import kotlin.test.BeforeTest

internal class FileDialogActionImpTest {
    lateinit var dialogStateMs:Ms<FileDialogState>
    lateinit var action:FileDialogActionImp
    @BeforeTest
    fun b(){
        dialogStateMs = ms(FileDialogStateImp(false))
        action = FileDialogActionImp(dialogStateMs)
    }
}