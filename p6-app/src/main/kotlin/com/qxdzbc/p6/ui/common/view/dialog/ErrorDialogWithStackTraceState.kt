package com.qxdzbc.p6.ui.common.view.dialog

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition

data class ErrorDialogWithStackTraceState(
    val messageMaxHeight: Dp = 100.dp,
    val stackTraceMaxHeight: Dp = 500.dp,
    val dialogWidth: Dp = 400.dp,
    val showStackTrace: Boolean = false,
    val copied: Boolean = false
) {
    companion object {
        val default = ErrorDialogWithStackTraceState()
    }
    val dialogHeight: Dp
        get() {
            return if (showStackTrace) {
                stackTraceMaxHeight + messageMaxHeight
            } else {
                messageMaxHeight
            } + 100.dp
        }

    fun switchShowStackTrace(): ErrorDialogWithStackTraceState {
        val v = !this.showStackTrace
        return this.copy(showStackTrace = v)
    }

    fun setCopied(v: Boolean): ErrorDialogWithStackTraceState {
        return this.copy(copied = v)
    }
    private var dialogPos: WindowPosition? = null
    val dialogState: DialogState
        get()  {
            val ds = DialogState(
                size = DpSize(
                    width = this.dialogWidth,
                    height = this.dialogHeight,
                ),
                position = dialogPos?: WindowPosition(Alignment.Center)
            )
            return ds
        }
}
