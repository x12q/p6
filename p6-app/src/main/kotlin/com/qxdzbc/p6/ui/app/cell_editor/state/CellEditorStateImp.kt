package com.qxdzbc.p6.ui.app.cell_editor.state

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.worksheet.check_range_selector_state.CheckRangeSelectorStateAction
import com.qxdzbc.p6.app.action.worksheet.check_range_selector_state.CheckRangeSelectorStateActionImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.DefaultTextFieldValue
import com.qxdzbc.p6.di.FalseMs
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.state.app_state.CellEditorInitCursorIdSt
import com.qxdzbc.p6.di.state.app_state.DefaultNullCellAddress
import com.qxdzbc.p6.di.state.app_state.NullTextFieldValue
import com.qxdzbc.p6.di.state.ws.cursor.DefaultCursorParseTree
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.squareup.anvil.annotations.ContributesBinding
import org.antlr.v4.runtime.tree.ParseTree
import javax.inject.Inject

@ContributesBinding(P6AnvilScope::class)
data class CellEditorStateImp @Inject constructor(
    @CellEditorInitCursorIdSt
    override val targetCursorIdSt: St<@JvmSuppressWildcards CursorStateId>?,
    @FalseMs
    override val isOpenMs: Ms<Boolean>,
    @DefaultTextFieldValue
    override val currentTextField: TextFieldValue = TextFieldValue(
        text = "",
        selection = TextRange(0)
    ),
    @DefaultNullCellAddress
    override val targetCell: CellAddress? = null,
    @CellEditorInitCursorIdSt
    override val rangeSelectorCursorIdSt: St<@JvmSuppressWildcards CursorStateId>? = null,
    @NullTextFieldValue
    override val rangeSelectorTextField: TextFieldValue? = null,
    @DefaultCursorParseTree
    override val parseTreeMs: Ms<ParseTree?> = StateUtils.ms(null),
    private val checkRangeSelector:CheckRangeSelectorStateAction,
) : CellEditorState {
    companion object {
        fun defaultForTest(): CellEditorStateImp {
            return CellEditorStateImp(null, false.toMs(), checkRangeSelector = CheckRangeSelectorStateActionImp())
        }
    }

    override val parseTree: ParseTree? get() = parseTreeMs.value
    override fun setParseTree(i: ParseTree?): CellEditorState {
        this.parseTreeMs.value = i
        return this
    }

    override fun clearAll(): CellEditorState {
        return this.clearAllText().setParseTree(null)
    }

    override fun stopGettingRangeAddress(): CellEditorState {
        val rt = this.moveTextFromRangeSelectorTextToCurrentText()
        return rt
    }

    private fun moveTextFromRangeSelectorTextToCurrentText(): CellEditorState {
        if (rangeSelectorTextField != null) {
            return this.setCurrentText(rangeSelectorTextField.text).setRangeSelectorTextField(null)
        } else {
            return this
        }
    }

    override val allowRangeSelector: Boolean
        get() {
            return checkRangeSelector.check(this.currentText,this.currentTextField.selection.end)
        }

    override val isOpen: Boolean by isOpenMs
    override val isNotOpen: Boolean
        get() = !isOpen
    override val isActiveAndAllowRangeSelector: Boolean
        get() = isOpen && allowRangeSelector
    override val currentText: String get() = currentTextField.text

    override val rangeSelectorCursorId: CursorStateId?
        get() = rangeSelectorCursorIdSt?.value

    override fun setRangeSelectorCursorId(i: St<CursorStateId>?): CellEditorState {
        return this.copy(rangeSelectorCursorIdSt = i)
    }

    override val targetCursorId: CursorStateId?
        get() = targetCursorIdSt?.value

    override fun setEditTarget(newCellAddress: CellAddress?): CellEditorState {
        return this.copy(targetCell = newCellAddress)
    }

    override val displayTextField: TextFieldValue
        get() {
            if (this.isOpen) {
                val rst: TextFieldValue = this.rangeSelectorTextField ?: this.currentTextField
                return rst
            } else {
                return this.currentTextField
            }
        }
    override val displayText: String
        get() = displayTextField.text
    override val rangeSelectorText: String?
        get() = rangeSelectorTextField?.text

    override fun setRangeSelectorTextField(newTextField: TextFieldValue?): CellEditorState {
        return this.copy(rangeSelectorTextField = newTextField)
    }

    override fun setCurrentText(newText: String): CellEditorState {
        val tf = this.currentTextField
        return this.copy(
            currentTextField = tf
                .copy(text = newText, selection = TextRange(newText.length))
        )
    }

    override fun setCurrentTextField(newTextField: TextFieldValue): CellEditorState {
        return this.copy(currentTextField = newTextField)
    }

    override fun setDisplayTextField(newTextField: TextFieldValue): CellEditorState {
        if (this.isActiveAndAllowRangeSelector) {
            return this.setRangeSelectorTextField(newTextField)
        } else {
            return this.setCurrentTextField(newTextField)
        }
    }

    override fun clearAllText(): CellEditorState {
        return this.setCurrentText("").setRangeSelectorTextField(null)
    }

    /**
     * Open this cell editor at the cursor whose id is [cursorIdMs]
     */
    override fun open(cursorIdMs: St<CursorStateId>): CellEditorState {
        isOpenMs.value = true
        return this.copy(targetCursorIdSt = cursorIdMs, rangeSelectorCursorIdSt = cursorIdMs)
    }

    override fun close(): CellEditorState {
        isOpenMs.value = false
        return this.copy(targetCursorIdSt = null, targetCell = null)
            .stopGettingRangeAddress()
            .setRangeSelectorTextField(null)
    }
}
