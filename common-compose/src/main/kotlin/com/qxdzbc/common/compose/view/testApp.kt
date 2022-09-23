package com.qxdzbc.common.compose.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun testApp(
    size:DpSize = DpSize(500.dp,500.dp),
    content: @Composable ()->Unit
) {
    application {
        val wState = rememberWindowState(size=size)
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test app",
            state = wState
        ) {
            content()
        }
    }
}
