package com.emeraldblast.p6.ui.window.kernel_dialog

import com.emeraldblast.p6.ui.window.kernel_dialog.start_kernel_dialog.StartKernelDialogState

interface ShowDialogState {
    val isShowing:Boolean
    fun show(): ShowDialogState
    fun hide(): ShowDialogState
}
