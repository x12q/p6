package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.TestApp
import com.qxdzbc.p6.ui.common.view.MBox

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val map = (1..120).associateBy(
        keySelector = { it },
        valueTransform = { "v${it}" }
    )
    TestApp {
        var mouseDown by rms(false)
        var text by rms("")
        var awtText by rms("")
        var selectionMap by rms((1..10).associateBy(
            keySelector = { it },
            valueTransform = { false }
        ))

        var posMap: Map<Int, Rect> by rms(emptyMap())

        var anchorPoint by rms(Offset(0F, 0F))
        var movingPoint by rms(Offset(0F, 0F))

        var selectRec by rms(Rect(Offset(0F, 0F), Offset(0F, 0F)))
        Column {
            Text(text)
            Text(awtText)
            MBox(modifier = Modifier
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.first().position
                            val awtEvent = event.awtEventOrNull
                            awtText = awtEvent?.toString() ?: ""
                            when (event.type) {
                                PointerEventType.Press -> {
                                    text = "Press"
                                    mouseDown = true
                                    anchorPoint = position
                                }
                                PointerEventType.Move -> {
                                    text = "Move"
                                    movingPoint = position
                                }
                                PointerEventType.Release -> {
                                    text = "Release"
                                    mouseDown = false
                                }
                            }

                            if (mouseDown) {
                                selectRec = Rect(anchorPoint, movingPoint)
                                for ((k, r) in posMap) {
                                    if (selectRec.intersect(r).isEmpty == false) {
                                        selectionMap = selectionMap + (k to true)
                                    } else {
                                        selectionMap = selectionMap + (k to false)
                                    }
                                }
                            }
                        }
                    }
                }) {
                Column {
                    for ((k, v) in map) {
                        MBox(modifier = Modifier
                            .size(100.dp, 50.dp)
                            .background(color = if (selectionMap[k] == true) Color.Cyan else Color.Transparent)
                            .onGloballyPositioned {
                                posMap = posMap + (k to it.boundsInParent())
                            }
                        ) {
                            Text(v)
                        }
                        Divider()
                    }

                }

                if (mouseDown) {
                    MBox(
                        modifier = Modifier
                            .size(
                                height = selectRec.height.dp,
                                width = (selectRec.width).dp
                            )
                            .offset(x = selectRec.topLeft.x.dp, y = selectRec.topLeft.y.dp)
                            .border(3.dp, Color.Black)
                    )
                }
            }
        }
    }
}
