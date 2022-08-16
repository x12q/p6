package com.emeraldblast.p6.ui.example

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.testApp
import com.emeraldblast.p6.ui.common.view.BorderBox

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    testApp(size = DpSize(150.dp,150.dp)) {
        val focusRequester = remember{FocusRequester()}
        BorderBox(
            modifier = Modifier
                .size(90.dp)
                .onPreviewKeyEvent {
                    when (it.key) {
                        Key.B -> {
                            println("**Parent")
                            true
                        }
                        else -> false
                    }
                }
        ) {
            BorderBox(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center)
                    .onPreviewKeyEvent {
                        when (it.key) {
                            Key.A -> {
                                println("to children view")
                            }
                            Key.B -> {
                                println("to parent view")
                            }
                        }
                        false
                    }
                    .focusRequester(focusRequester)
                    .focusable()

            ) {
                BorderBox(
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                        .onPreviewKeyEvent {
                            when (it.key) {
                                Key.A -> {
                                    println("**Child")
                                    true
                                }
                                else -> false
                            }
                        }
                ) {
                    Text("V3")
                }
            }
        }
        DisposableEffect(Unit){
            focusRequester.requestFocus()
            onDispose {  }
        }
    }
}
