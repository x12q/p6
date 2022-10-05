package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.input.TextFieldValue
import org.apache.commons.text.diff.StringsComparator
import javax.inject.Inject

class TextDifferImp @Inject constructor() : TextDiffer {

    override fun extractTextAddition(oldTextField: TextFieldValue, newTextFieldValue: TextFieldValue): TextAndRange? {

        val comparator = StringsComparator(oldTextField.text, newTextFieldValue.text)
        val visitor = CharCommandsVisitor()
        comparator.script.visit(visitor)

            return TextAndRange(
                text = visitor.addition,
                range = newTextFieldValue.selection
            )
    }
}
