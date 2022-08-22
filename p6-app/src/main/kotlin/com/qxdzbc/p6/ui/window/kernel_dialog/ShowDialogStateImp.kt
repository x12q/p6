package com.qxdzbc.p6.ui.window.kernel_dialog

data class ShowDialogStateImp(override val isShowing: Boolean = false) : ShowDialogState {
    override fun show(): ShowDialogStateImp {
        return this.copy(isShowing = true)
    }

    override fun hide(): ShowDialogStateImp {
        return this.copy(isShowing = false)
    }

}
