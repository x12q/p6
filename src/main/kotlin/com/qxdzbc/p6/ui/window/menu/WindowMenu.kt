package com.qxdzbc.p6.ui.window.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import com.qxdzbc.p6.ui.window.menu.action.CodeMenuAction
import com.qxdzbc.p6.ui.window.menu.action.FileMenuAction
import com.qxdzbc.p6.ui.window.state.WindowState


@Composable
fun FrameWindowScope.WindowMenu(
    fileMenuAction: FileMenuAction?,
    codeMenuAction: CodeMenuAction?,
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
            Item("Save", mnemonic = 's', enabled = windowState.activeWorkbookState != null) {
                fileMenuAction?.save(windowState.id)
            }
            Item("Save as...", enabled = windowState.activeWorkbookState != null) {
                fileMenuAction?.saveAs(windowState.id)
            }

        }
        Menu("Code", mnemonic = 'c') {
            Item("Open code editor", enabled = true) {
                codeMenuAction?.openCodeEditor()
            }
            Item("Start kernel with Python", enabled =true){
                codeMenuAction?.openDialogToStartKernel(windowState.id)
            }
            Item("Connect to existing kernel", enabled =true){
                codeMenuAction?.openDialogToConnectToKernel(windowState.id)
            }
            Item("Stop kernel", enabled =  windowState.kernel.kernelStatus.isProcessUnderManagement){
                codeMenuAction?.stopKernel()
            }
        }
    }
}
