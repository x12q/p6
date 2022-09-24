package test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun testRunApp(
    size: DpSize = DpSize(500.dp,500.dp),
    content: @Composable TestAppScope.()->Unit
) {
    application {
        val wState = rememberWindowState(size=size)
        val testScope = remember{TestSample()}
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test app",
            state = wState
        ) {
            content(testScope)
        }
    }
}

