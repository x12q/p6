package com.qxdzbc.p6.ui.window.tool_bar.color_selector.color_dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.p6.ui.common.view.dialog.Dialogs

@Composable
fun ColorDialog(
    onPick: (color: Color) -> Unit = {},
    onCancel: ()->Unit = {}
) {
    var color by rms(Color.Transparent)
    Dialogs.SingleItemMDialog(
        title="pick color",
        size = DpSize(300.dp,300.dp),
        onOk = {
            onPick(color)
        },
        onCancel = onCancel,
        onCloseRequest = onCancel,
    ) {
        MBox(modifier = Modifier.size(300.dp,250.dp)){
            ClassicColorPicker(
                modifier = Modifier.align(Alignment.Center).padding(10.dp),
                onColorChanged = { hsvColor: HsvColor ->
                    color = hsvColor.toColor()
                }
            )
        }
    }
}


fun main() = P6TestApp {
    ColorDialog()
}
