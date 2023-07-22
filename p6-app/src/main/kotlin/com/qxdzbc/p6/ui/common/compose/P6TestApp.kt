package com.qxdzbc.p6.ui.common.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * An empty window for testing running composables
 */
@Deprecated("dont use, use testApp{} instead")
fun P6TestApp(size: DpSize = DpSize(1500.dp, 600.dp), content: @Composable ApplicationScope.() -> Unit) {
    application {
        val wState = rememberWindowState(size=size)
        val appScope = this
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test app",
            state = wState
        ) {
            P6Theme{
                content(appScope)
            }
        }
    }
}
