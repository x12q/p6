package com.emeraldblast.p6.ui.common.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.emeraldblast.p6.ui.theme.P6DefaultTypoGraphy
import com.emeraldblast.p6.ui.theme.P6LightColors2

fun TestApp(size: DpSize = DpSize(1500.dp, 600.dp), content: @Composable ApplicationScope.() -> Unit) = application {
    val appScope = this
    val windowState = rememberWindowState(size = size)
    Window(onCloseRequest = ::exitApplication, windowState) {
        MaterialTheme(colors = P6LightColors2, typography = P6DefaultTypoGraphy){
            content(appScope)
        }
    }
}
