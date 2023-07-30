package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.input.TextFieldValue



interface TextDiffer {
    /**
     * Extract text addition when [oldTextFieldValue] transforms into [newTextFieldValue]. With one exception, the return [TextAndRange] always contain the selection [newTextFieldValue]
     */
    fun extractTextAdditionWithRangeOfNewText(
        oldTextFieldValue:TextFieldValue,
        newTextFieldValue: TextFieldValue,
    ):TextAndRange?

    /**
     * run a text diff between 2 text value.
     */
    fun runTextDif(oldTextFieldValue:TextFieldValue, newTextFieldValue: TextFieldValue):TextDiffResult
}

