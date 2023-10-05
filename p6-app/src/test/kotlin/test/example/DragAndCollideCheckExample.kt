package test.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
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
import com.qxdzbc.common.compose.LayoutCoorsUtils.toP6Layout
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.OffsetUtils.rawConvertToIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.common.compose.view.MBox

/**
 * dragging a green box over a red box, detect when they overlap.
 * Need:
 * - a surface to track mouse position
 * - a drag obj: must have it position cached, a  nullable variable to hold its position
 * - a target : target is where a certain action is trigger when the dragged obj overlap it.
 * Any way to make this a general solution? I rather not touch the swing layer.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(
        state = WindowState(width = 350.dp, height = 450.dp),
        onCloseRequest = ::exitApplication
    ) {
        var pos: IntOffset? by rms(null)
        var isClicked by rms(false)

        val greenBoxPosMs: Ms<P6Layout?> = rms(null)
        val redBoxPosMs: Ms<P6Layout?> = rms(null)
        var dragHostCoorWrapper: P6Layout? by rms(null)

        // drag host
        Surface(color = Color.Black, modifier = Modifier
            .onPointerEvent(PointerEventType.Move) {
                if (isClicked) {
                    it.changes.getOrNull(0)?.position?.also { mousePos ->
                        dragHostCoorWrapper?.let { cp ->
                            pos = cp.localToWindow(mousePos).rawConvertToIntOffset()
                        }
                    }
                }
            }
        ) {
            val isOverlap = greenBoxPosMs.value?.boundInWindow?.let { gb ->
                redBoxPosMs.value?.boundInWindow?.let { rb ->
                    gb.overlaps(rb)
                }
            } ?: false
            Column(modifier = Modifier.onGloballyPositioned { dragHostCoorWrapper = it.toP6Layout() }) {
                Text("Overlap: $isOverlap", color = Color.White)
                Box(modifier = Modifier.fillMaxSize()) {
                    // drag target
                    MBox(
                        modifier = Modifier.size(120.dp, 30.dp).offset(0.dp, 100.dp).background(Color.Red)
                            .onGloballyPositioned {
                                redBoxPosMs.value = it.toP6Layout()
                            }
                    )

                    // drag obj
                    MBox(
                        modifier = Modifier.size(50.dp)
                            .let { mod ->
                                pos?.let {
                                    mod.offset { it }
                                } ?: mod
                            }.background(Color.Green)
                            .onPointerEvent(PointerEventType.Press) {
                                isClicked = true
                            }.onPointerEvent(PointerEventType.Release) {
                                isClicked = false
                                if (!isOverlap) {
                                    pos = null
                                }
                            }.onGloballyPositioned {
                                greenBoxPosMs.value = it.toP6Layout()
                            })
                }
            }
        }
    }


}
