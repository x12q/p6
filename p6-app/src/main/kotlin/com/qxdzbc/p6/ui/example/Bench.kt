package com.qxdzbc.p6.ui.example

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.view.MBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    application{

    }
}

@Composable
fun Sy(){
    MBox(modifier = Modifier.size(100.dp,100.dp).background(Color.Green)) {
        Column{
            Text("A Text")
            Button(onClick={}){
                Text("Bt")
            }
        }

    }
}

@Preview()
@Composable
fun SyPrev(){
    Sy()
}
