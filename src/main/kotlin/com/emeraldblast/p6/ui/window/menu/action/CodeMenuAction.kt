package com.emeraldblast.p6.ui.window.menu.action

interface CodeMenuAction {
    fun openCodeEditor()
    fun stopKernel()
    fun openDialogToStartKernel(windowId: String)
    fun openDialogToConnectToKernel(windowId: String)
}
