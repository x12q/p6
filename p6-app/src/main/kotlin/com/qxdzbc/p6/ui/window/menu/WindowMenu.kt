package com.qxdzbc.p6.ui.window.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import com.qxdzbc.p6.ui.window.menu.action.FileMenuAction
import com.qxdzbc.p6.ui.window.state.WindowState


@Composable
fun FrameWindowScope.WindowMenu(
    fileMenuAction: FileMenuAction?,
    windowState: WindowState
) {
    MenuBar {
        Menu("File", mnemonic = 'f') {
            Item("New workbook", enabled = true) {
                fileMenuAction?.newWorkbook(windowState.id)
            }
            Item("Open", mnemonic = 'o', enabled = true) {
                fileMenuAction?.open(windowState.id)
            }
            Item("Save", mnemonic = 's', enabled = windowState.activeWbState != null) {
                fileMenuAction?.save(windowState.id)
            }
            Item("Save as...", enabled = windowState.activeWbState != null) {
                fileMenuAction?.saveAs(windowState.id)
            }
            Item("Close workbook", enabled = windowState.activeWbState != null) {
                fileMenuAction?.closeActiveWorkbook(windowState.id)
            }

        }
    }
}
