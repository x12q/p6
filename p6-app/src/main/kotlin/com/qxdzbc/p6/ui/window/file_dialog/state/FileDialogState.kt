package com.qxdzbc.p6.ui.window.file_dialog.state

interface FileDialogState {
    val isOpen:Boolean
    fun setOpen(v:Boolean): FileDialogState
}


