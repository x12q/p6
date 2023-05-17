package com.qxdzbc.p6.ui.common.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.qxdzbc.p6.ui.theme.P6DefaultTypoGraphy
import com.qxdzbc.p6.ui.theme.P6LightColors2

/**
 * An empty window for testing running composables
 */
fun P6TestApp(size: DpSize = DpSize(1500.dp, 600.dp), content: @Composable ApplicationScope.() -> Unit) {
    application {
        val wState = rememberWindowState(size=size)
        val appScope = this
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test app",
            state = wState
        ) {
            MaterialTheme(colors = P6LightColors2, typography = P6DefaultTypoGraphy){
                content(appScope)
            }
        }
    }
}
