package com.qxdzbc.common.compose.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

enum class TestAppWindowSize{
    Small, Medium, Big
}

fun testApp(
    size:TestAppWindowSize = TestAppWindowSize.Small,
    content: @Composable ApplicationScope.() -> Unit
){
    val s = when(size){
        TestAppWindowSize.Small -> DpSize(500.dp,500.dp)
        TestAppWindowSize.Medium -> DpSize(1500.dp, 600.dp)
        TestAppWindowSize.Big -> DpSize(1500.dp, 600.dp)
    }
    testApp(s,content)
}

fun testApp(
    dpSize: DpSize,
    content: @Composable ApplicationScope.()->Unit
) {
    application {
        val wState = rememberWindowState(size=dpSize)
        val appScope = this
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test app",
            state = wState
        ) {
            content(appScope)
        }
    }
}
