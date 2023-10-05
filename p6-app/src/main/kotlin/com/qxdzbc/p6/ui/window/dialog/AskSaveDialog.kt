package com.qxdzbc.p6.ui.window.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.common.compose.view.testApp

import com.qxdzbc.p6.ui.common.view.MButton
import com.qxdzbc.p6.ui.common.view.CenterAlignRow
import com.qxdzbc.p6.ui.common.view.dialog.Dialogs

@Composable
fun AskSaveDialog(
    wbName: String?,
    onDontSave: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Dialogs.DraggableDialog(
        title = "Save document?",
        onCloseRequest = onCancel,
        size = DpSize(500.dp,140.dp)
    ) {
        Column {
            MBox(modifier= Modifier.padding(start=10.dp,end = 10.dp,top=10.dp,bottom=20.dp)){
                if(wbName!=null){
                    Text("$wbName contains unsaved changes.They will be lost if you close it without saving. Save them?")
                }else{
                    Text("The active workbook contains unsaved changes.They will be lost if you close it without saving. Save them?")
                }
            }

            CenterAlignRow(horizontalArrangement = Arrangement.SpaceEvenly,modifier =Modifier.fillMaxWidth()) {
                MButton(label = "Save", onClick = onSave)
                MButton(label = "Don't save", onClick = onDontSave)
                MButton(label = "Cancel", onClick = onCancel)
            }
        }
    }
}


fun main() {
    testApp {
        AskSaveDialog(
            wbName = "wb1.qwe",
            onDontSave = {},
            onSave = {},
            onCancel = {}
        )
    }
}
