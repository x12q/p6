package com.qxdzbc.p6.ui.document.workbook.dialog

import androidx.compose.runtime.Composable
import com.qxdzbc.p6.ui.common.view.dialog.Dialogs

@Composable
fun DeleteWorksheetDialog(
    sheetName:String,
    onOk: (sheetName:String)->Unit,
    onCancel:()->Unit
) {

    Dialogs.MessageDialog (
        text= "Delete ${sheetName}?",
        onOk = {onOk(sheetName)},
        onCancel=onCancel)
}
