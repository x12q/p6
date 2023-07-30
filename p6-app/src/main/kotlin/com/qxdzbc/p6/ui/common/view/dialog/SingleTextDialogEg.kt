package com.qxdzbc.p6.ui.common.view.dialog

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.theme.P6Theme


fun main() {
    P6TestApp() {
        P6Theme  {
            MBox(modifier = Modifier.border(1.dp, P6Theme.color.uiColor.borderColor)) {
                Dialogs.MessageDialog ()
            }
        }
    }
}
