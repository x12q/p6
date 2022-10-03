package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.TextRange
import com.qxdzbc.common.CanCheckEmpty

data class TextAndRange(
    val text:String,
    val range: TextRange
): CanCheckEmpty {
    companion object {
        val empty = TextAndRange("", TextRange.Zero)
    }

    override fun isEmpty(): Boolean {
        return text.isEmpty()
    }
}
