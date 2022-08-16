package com.emeraldblast.p6.ui.window.file_dialog.state

data class FileDialogStateImp(
    override val isOpen: Boolean = false
): FileDialogState {
    override fun setOpen(v: Boolean): FileDialogState {
        return this.copy(isOpen=v)
    }
}
