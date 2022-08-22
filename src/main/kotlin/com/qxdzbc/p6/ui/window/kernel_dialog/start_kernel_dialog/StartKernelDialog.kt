package com.qxdzbc.p6.ui.window.kernel_dialog.start_kernel_dialog

import androidx.compose.runtime.Composable
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.view.dialog.Dialogs

@Composable
fun StartKernelDialog(
    onOk: (newText: String) -> Unit,
    onCancel:()->Unit,
    openBrowserToUpdatePath: (text:Ms<String>)-> Unit = {},
    ){
    Dialogs.DirectoryDialog(
        title = "Start kernel",
        initText ="",
        onOk = onOk,
        onCancel = onCancel,
        openBrowserAndUpdateText = openBrowserToUpdatePath
    )
}
