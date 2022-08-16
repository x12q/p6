package com.emeraldblast.p6.ui.d_menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * provided menu, better
 */
fun main() = application {
    val applicationState = remember { MyApplicationState() }

    for (window in applicationState.windows) {
        key(window) {
            MyWindow(window)
        }
    }
}

@Composable
private fun ApplicationScope.MyWindow(
    state: MyWindowState
) = Window(onCloseRequest = state::close, title = state.title) {
    MenuBar {
        Menu("File",mnemonic = 'f') {
            Item("New window", onClick = state.openNewWindow)
            Item("Exit", onClick = state.exit)
            Menu("Nested"){
                Item("ABC", onClick ={})
            }
        }

        Menu("Edit"){
            Item("Q"){}
            Item("123"){}
        }
    }
}

private class MyApplicationState {
    val windows = mutableStateListOf<MyWindowState>()

    init {
        windows += MyWindowState("Initial window")
    }

    fun openNewWindow() {
        windows += MyWindowState("Window ${windows.size}")
    }

    fun exit() {
        windows.clear()
    }

    private fun MyWindowState(
        title: String
    ) = MyWindowState(
        title,
        openNewWindow = ::openNewWindow,
        exit = ::exit,
        windows::remove
    )
}

private class MyWindowState(
    val title: String,
    val openNewWindow: () -> Unit,
    val exit: () -> Unit,
    private val close: (MyWindowState) -> Unit
) {
    fun close() = close(this)
}
