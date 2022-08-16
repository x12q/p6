package com.emeraldblast.p6.ui.common.view.dialog

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition

data class ErrorDialogWithStackTraceState(
    val messageMaxHeight: Int = 100,
    val stackTraceMaxHeight: Int = 500,
    val dialogWidth: Int = 400,
    val showStackTrace: Boolean = false,
    val copied: Boolean = false
) {
    companion object {
        val default = ErrorDialogWithStackTraceState()
    }
    val dialogHeight: Int
        get() {
            return if (showStackTrace) {
                stackTraceMaxHeight + messageMaxHeight
            } else {
                messageMaxHeight
            } + 100
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
                    width = this.dialogWidth.dp,
                    height = this.dialogHeight.dp,
                ),
                position = dialogPos?: WindowPosition(Alignment.Center)
            )
            return ds
        }
}
