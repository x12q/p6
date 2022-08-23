package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.TestApp
import com.qxdzbc.p6.ui.common.view.MBox

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    TestApp {
        val text = rms("")
        val topOffset = rms(0)
        val rightOffset = rms(0)
        val focusRequester = remember { FocusRequester() }
        val cornerPos = rms(Offset(0F, 0F))
        val size = rms(IntSize(0, 0))
        MBox(modifier = Modifier.fillMaxSize()){
            MBox(modifier = Modifier
                .size(100.dp, 100.dp)
                .offset(y = topOffset.value.dp, x = rightOffset.value.dp)
                .onGloballyPositioned { layoutCoor ->
                    text.value = layoutCoor.size.toString()
                    cornerPos.value = layoutCoor.positionInWindow()
                    size.value = layoutCoor.size
                }
                .background(color = Color.Cyan)
                .onPreviewKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.DirectionDown -> {
                            topOffset.value = maxOf(topOffset.value + 20, 0)
                        }
                        Key.DirectionUp -> {
                            topOffset.value = maxOf(topOffset.value - 20, 0)
                        }
                        Key.DirectionLeft -> {
                            rightOffset.value = maxOf(rightOffset.value - 20, 0)
                        }
                        Key.DirectionRight -> {
                            rightOffset.value = maxOf(rightOffset.value + 20, 0)
                        }
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable()
            ) {
                println("redner blue box")
                BasicText(text.value)
            }
            println("zxc")

            MBox(modifier = Modifier
                .size(20.dp, 20.dp)
                .offset(x = (cornerPos.value.x.toInt() + size.value.width/2-10).dp, y = (cornerPos.value.y.toInt()+size.value.height/2-10).dp)
                .background(color = Color.Red)
            ) {

                println("render red box")
                BasicText("X")
            }
        }

        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }
    }
}
