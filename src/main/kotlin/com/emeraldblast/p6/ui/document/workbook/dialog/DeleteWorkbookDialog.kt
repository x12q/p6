package com.emeraldblast.p6.ui.document.workbook.dialog

import androidx.compose.runtime.Composable
import com.emeraldblast.p6.ui.common.view.dialog.Dialogs

@Composable
fun DeleteWorksheetDialog(
    sheetName:String,
    onOk: (sheetName:String)->Unit,
    onCancel:()->Unit
) {

    Dialogs.SingleTextDialog (
        text= "Delete ${sheetName}?",
        onOk = {onOk(sheetName)},
        onCancel=onCancel)
}
