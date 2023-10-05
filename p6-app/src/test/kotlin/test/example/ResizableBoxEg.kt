package test.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.OffsetUtils.rawConvertToIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox

import com.qxdzbc.p6.ui.common.view.BorderBox

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    testApp (dpSize = DpSize(300.dp, 300.dp)) {

        var width by rms(50.dp)
        var anchorWidth by rms(50.dp)
        var mouseDown by rms(false)

        var anchorMousePos: Offset? by rms(null)
        var currentMousePos: Offset? by rms(null)
        var diff: Float by rms(0.0F)
        var lc: LayoutCoordinates? by rms(null)
        var thumbLc: LayoutCoordinates? by rms(null)
        MBox(modifier = Modifier.fillMaxSize().onGloballyPositioned {
            lc = it
        }) {
            Box(
                modifier = Modifier.size(10.dp).offset {
                    val l = lc
                    if (l != null && l.isAttached) {
                        l.windowToLocal(currentMousePos ?: Offset(0F, 0F)).rawConvertToIntOffset()
                    } else {
                        IntOffset(0, 0)
                    }
                }.background(Color.Blue)
            )
            val density = LocalDensity.current

            Column {
                Text("Anchor mouse: ${anchorMousePos ?: ""}")
                BasicText("current mouse: ${currentMousePos ?: ""}")
                BasicText("anchor width :${anchorWidth}")
                BasicText("width :${width}")
                BasicText("diff: ${diff}")
                BorderBox(modifier = Modifier.size(width = width, height = 50.dp)) {
                    BorderBox(
                        modifier = Modifier
                            .onGloballyPositioned {
                                thumbLc = it
                            }
                            .height(50.dp)
                            .width(20.dp)
                            .align(Alignment.CenterEnd)
                            .background(Color.Red)
                            .onPointerEvent(PointerEventType.Press) {
                                anchorWidth = width
                                mouseDown = true
                                val tlc = thumbLc
                                if (tlc != null && tlc.isAttached) {
                                    anchorMousePos = tlc.localToWindow(it.changes.first().position)
                                }
                            }.onPointerEvent(PointerEventType.Release) {
                                mouseDown = false
                            }.onPointerEvent(PointerEventType.Move) {
                                val tlc = thumbLc
                                if (tlc != null && tlc.isAttached) {
                                    currentMousePos = tlc.localToWindow(it.changes.first().position)
                                }
                                if (mouseDown) {
                                    val a = anchorMousePos
                                    val c = currentMousePos
                                    if (a != null && c != null) {
                                        width  = with(density){
                                            anchorWidth + (c.x - a.x).toDp()
                                        }
                                    }
                                }

                            }
                    )
                }

            }

        }

    }
}
