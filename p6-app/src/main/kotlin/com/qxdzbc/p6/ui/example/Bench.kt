package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.view.MBox


@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(
        state = WindowState(width = 350.dp, height = 450.dp),
        onCloseRequest = ::exitApplication
    ) {
        var x by rms(1)
        var y by rms(2)
        val z by remember{ derivedStateOf { x+y }}
        Column{
            Text("x: $x")
            Text("y: $y")
            Text("z: $z")
            MButton("up x") {
                x=x+1
            }
            MButton("up y"){
                y = y+1
            }
        }
    }
}
