package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.input.TextFieldValue



interface TextDiffer {
    /**
     * Extract text addition when [oldTextFieldValue] transforms into [newTextFieldValue].
     * Return null if the transforming action is not addition.
     * TODO this text differ is very inadequate. It can only tell if the transformation is addition or not.
     * TODO And it does not even give correct addition text range. Should not use this.
     *
     */
    fun extractTextAddition(oldTextFieldValue:TextFieldValue, newTextFieldValue: TextFieldValue):TextAndRange?
    fun runTextDif(oldTextFieldValue:TextFieldValue, newTextFieldValue: TextFieldValue):TextDiffResult
}

