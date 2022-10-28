package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import org.apache.commons.text.diff.StringsComparator
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
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
