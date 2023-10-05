package com.qxdzbc.p6.ui.common.view.dialog

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.theme.P6Theme

fun main() {
    testApp {
        P6Theme  {
            MBox(modifier = Modifier.border(1.dp, P6Theme.color.uiColor.borderColor)) {
                Dialogs.DirectoryDraggableDialog(
                    title="Dir",
                    initText="123",
                    onOk = {
                           println(it)
                    },
                    openBrowserAndUpdateText = {
                        println("click dir")
                        null
                    }
                )
            }
        }
    }
}
