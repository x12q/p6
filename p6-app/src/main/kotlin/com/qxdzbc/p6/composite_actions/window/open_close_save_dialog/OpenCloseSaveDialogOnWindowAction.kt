package com.qxdzbc.p6.composite_actions.window.open_close_save_dialog

interface OpenCloseSaveDialogOnWindowAction{
    fun openSaveFileDialog(windowId: String)
    fun closeSaveFileDialog(windowId: String)
}
