package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    P6TestApp {
        val numberMs = rms(123)
        val cc = rememberCoroutineScope()

        var t1: String by rms("t1")
        var t2: String by rms("t2")
        var f by rms(true)
        val fc = remember { FocusRequester() }
        val fc2 = remember { FocusRequester() }
        LaunchedEffect(f) {
            if (f) {
                fc.requestFocus()
            } else {
                fc2.requestFocus()
            }
        }
        Column {
            Box(modifier= Modifier.border(1.dp, Color.Black)){
                BasicTextField(
                    value = "number ${numberMs.value}",
                    onValueChange = { t1 = it },
                    modifier = Modifier.focusRequester(fc),
                )
            }
            MButton("Start") {
                cc.launch(Dispatchers.Default) {
                    while(true){
                        numberMs.value = numberMs.value+1
                        delay(1000)
                    }
                }
            }
        }
    }
}
