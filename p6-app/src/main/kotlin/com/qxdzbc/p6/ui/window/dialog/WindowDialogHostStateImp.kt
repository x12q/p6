package com.qxdzbc.p6.ui.window.dialog

data class WindowDialogHostStateImp(
    override val isAskSaveOpen: Boolean = false
) : WindowDialogHostState {
    override fun setIsAskSaveOpen(i: Boolean): WindowDialogHostStateImp {
        return this.copy(isAskSaveOpen=i)
    }
}
