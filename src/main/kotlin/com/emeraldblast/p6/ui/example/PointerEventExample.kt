package com.emeraldblast.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.PointerEventUtils.consumeAllPressed
import com.emeraldblast.p6.ui.common.compose.PointerEventUtils.hasPressedChanges
import com.emeraldblast.p6.ui.common.compose.TestApp

//import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    TestApp(size = DpSize(300.dp, 300.dp)) {
        val focusRequester: FocusRequester = remember { FocusRequester() }

        Box(modifier = Modifier
            .size(100.dp, 100.dp)
            .background(Color.Green)
            .onPointerEvent(PointerEventType.Press) {
                if(it.hasPressedChanges()){
                    println("Outer click")
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .offset(50.dp, 50.dp)
                    .background(Color.Red)
                    .onPointerEvent(PointerEventType.Press) {
                        it.consumeAllPressed()
                        println("Inner click")
                    }
                    .onPointerEvent(PointerEventType.Release){
                        println("inner release")
                    }
                    .focusRequester(focusRequester)
                    .focusable()
            ) {

            }


        }

        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }
    }
}
