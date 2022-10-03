package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.input.TextFieldValue


interface TextDiffer {
    /**
     * Extract text addition, return null if the interaction is not addition
     */
    fun extractTextAddition(oldTextField:TextFieldValue, newTextFieldValue: TextFieldValue):TextAndRange?
}

