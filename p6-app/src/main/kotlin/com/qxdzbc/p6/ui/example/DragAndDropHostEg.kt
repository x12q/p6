package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.drag_drop.*
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.view.MBox

/**
 * dragging a green box over a red box, detect when they overlap.
 */
fun main() = application {
    Window(
        state = WindowState(width = 350.dp, height = 450.dp),
        onCloseRequest = ::exitApplication
    ) {
        val green = "green"
        val red = "red"
        val blue = "blue"
        // drag host
        val stateMs: Ms<DragAndDropHostState> = rms(DragAndDropHostStateImp())

        val greenBoxPos: LayoutCoorWrapper? = stateMs.value.dragMap[green]
        val redBoxPos: LayoutCoorWrapper? = stateMs.value.dropMap[red]

        val isOverlap = greenBoxPos?.boundInWindow?.let { gb ->
            redBoxPos?.boundInWindow?.let { rb ->
                gb.overlaps(rb)
            }
        } ?: false
        DragAndDropHost(
            internalStateMs = stateMs,
        ) {
            Column {
                Text("Overlap: $isOverlap", color = Color.White)
                MBox(modifier = Modifier.fillMaxSize().background(Color.Gray)) {
                    Drag(
                        internalStateMs = stateMs,
                        identifier = { blue }
                    ) {
                        MBox(
                            modifier = Modifier.size(50.dp)
                                .background(Color.Blue)
                        )
                    }

                    Drop(
                        internalStateMs = stateMs,
                        identifier = { red }
                    ) {
                        MBox(
                            modifier = Modifier.size(120.dp, 30.dp).offset(0.dp, 100.dp).background(Color.Red)
                        )
                    }
                }
            }
        }
    }
}
