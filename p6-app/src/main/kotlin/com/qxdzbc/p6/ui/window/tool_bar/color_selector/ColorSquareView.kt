package com.qxdzbc.p6.ui.window.tool_bar.color_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.view.BorderBox

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorSquareView(
    color: Color
) {
    var borderColor by rms(Color.Transparent)
    BorderBox(
        borderColor = borderColor,
        modifier = Modifier.onPointerEvent(PointerEventType.Enter) {
            borderColor = Color.LightGray
        }.onPointerEvent(PointerEventType.Exit){
            borderColor = Color.Transparent
        }
    ) {
        Box(modifier = Modifier.size(22.dp).padding(2.dp).background(color))
    }
}
