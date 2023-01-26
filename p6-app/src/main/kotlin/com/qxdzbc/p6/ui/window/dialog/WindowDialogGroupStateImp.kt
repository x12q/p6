package com.qxdzbc.p6.ui.window.dialog

data class WindowDialogGroupStateImp(
    override val isAskSaveOpen: Boolean = false
) : WindowDialogGroupState {
    override fun setIsAskSaveOpen(i: Boolean): WindowDialogGroupStateImp {
        return this.copy(isAskSaveOpen=i)
    }
}
