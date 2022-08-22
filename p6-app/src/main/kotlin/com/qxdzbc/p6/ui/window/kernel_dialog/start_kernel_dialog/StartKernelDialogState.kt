package com.qxdzbc.p6.ui.window.kernel_dialog.start_kernel_dialog

interface StartKernelDialogState {
    val isShowing:Boolean
    fun show(): StartKernelDialogState
    fun hide(): StartKernelDialogState
}
