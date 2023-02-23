package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        val cyan = "cyan"
        // drag host
        val stateMs: Ms<DragAndDropHostState> = rms(DragAndDropHostStateImp())
        val state by stateMs

        val isBlueOverlap = state.dragMap[blue]?.boundInWindow?.let { bb ->
            state.dropMap[red]?.boundInWindow?.let { rb ->
                bb.overlaps(rb)
            }
        } ?: false
        val isGreenOverlap = state.dragMap[green]?.boundInWindow?.let { gb ->
            state.dropMap[red]?.boundInWindow?.let { rb ->
                gb.overlaps(rb)
            }
        } ?: false
        DragDropHost(
            internalStateMs = stateMs,
        ) {
            var dropEventText by rms("")
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Blue: ${state.dragMap[blue]?.boundInWindow}", color = Color.White)
                Text("Green: ${state.dragMap[green]?.boundInWindow}", color = Color.White)
                Text("Red: ${state.dropMap[red]?.boundInWindow}", color = Color.White)
                Text("Blue overlap: $isBlueOverlap", color = Color.White)
                Text("Green overlap: $isGreenOverlap", color = Color.White)
                Text("Drop event: ${dropEventText}", color = Color.White)

                MBox(modifier = Modifier.fillMaxWidth().height(30.dp).border(1.dp, Color.Red).background(Color.Gray)) {
                    Text("Padding", modifier = Modifier.align(Alignment.Center))
                }
                MBox(modifier = Modifier.fillMaxSize()) {
                    Row {
                        Drag(
                            internalStateMs = stateMs,
                            identifier = blue,
                            onDrop = {dragId,dropId->
                                dropEventText="drop ${dragId} on ${dropId}"
                            }
                        ) {
                            MBox(
                                modifier = Modifier.size(50.dp)
                                    .background(Color.Blue)
                            )
                        }

                        Drag(
                            internalStateMs = stateMs,
                            identifier = green,
                            onDrop = {dragId,dropId->
                                dropEventText="drop ${dragId} on ${dropId}"
                            }
                        ) {
                            MBox(
                                modifier = Modifier.size(50.dp)
                                    .background(Color.Green)
                            )
                        }
                    }

                    Row(modifier = Modifier.offset(0.dp, 100.dp)){
                        Drop(
                            internalStateMs = stateMs,
                            identifier = { red },
                        ) {
                            MBox(
                                modifier = Modifier.size(120.dp, 30.dp).background(Color.Red)
                            )
                        }
                        Drop(
                            internalStateMs = stateMs,
                            identifier = { cyan },
                        ) {
                            MBox(
                                modifier = Modifier.size(120.dp, 30.dp).background(Color.Cyan)
                            )
                        }
                    }
                }
            }
        }
    }
}
