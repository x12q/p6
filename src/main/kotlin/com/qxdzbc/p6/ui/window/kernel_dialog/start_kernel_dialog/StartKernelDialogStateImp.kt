package com.qxdzbc.p6.ui.window.kernel_dialog.start_kernel_dialog

data class StartKernelDialogStateImp(override val isShowing: Boolean = false) : StartKernelDialogState {
    override fun show(): StartKernelDialogState {
        return this.copy(isShowing = true)
    }

    override fun hide(): StartKernelDialogState {
        return this.copy(isShowing = false)
    }

}
