package com.qxdzbc.p6

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() {
    application {
        Window(
            state = rememberWindowState(),
            onCloseRequest = {this.exitApplication()}
        ){
            Column {
                Box(Modifier.fillMaxHeight().width(30.dp).background(Color.Red))
                Column(Modifier.verticalScroll(rememberScrollState())) {

//                Box(Modifier.size(30.dp).background(Color.Red))
                    for(x in 1..100){
                        Box(Modifier.size(30.dp).background(Color.Blue))
                        Box(Modifier.size(30.dp).background(Color.Green))
                    }

                }
            }

        }
    }
}