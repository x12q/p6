package com.qxdzbc.p6.app.action.window.open_close_save_dialog

interface OpenCloseSaveDialogOnWindowAction{
    fun openSaveFileDialog(windowId: String)
    fun closeSaveFileDialog(windowId: String)
}
