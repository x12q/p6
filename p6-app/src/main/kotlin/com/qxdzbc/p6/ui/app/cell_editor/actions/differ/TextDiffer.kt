package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.input.TextFieldValue


interface TextDiffer {
    /**
     * Extract text addition when [oldTextFieldValue] transforms into [newTextFieldValue].
     * Return null if the transforming action is not addition.
     */
    fun extractTextAddition(oldTextFieldValue:TextFieldValue, newTextFieldValue: TextFieldValue):TextAndRange?
}

