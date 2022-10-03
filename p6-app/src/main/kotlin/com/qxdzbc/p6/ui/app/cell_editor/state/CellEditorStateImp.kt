package com.qxdzbc.p6.ui.app.cell_editor.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.DefaultTextFieldValue
import com.qxdzbc.p6.di.False
import com.qxdzbc.p6.di.FalseMs
import com.qxdzbc.p6.di.state.app_state.CellEditorInitCursorIdSt
import com.qxdzbc.p6.di.state.app_state.DefaultNullCellAddress
import com.qxdzbc.p6.di.state.app_state.NullTextFieldValue
import com.qxdzbc.p6.ui.app.cell_editor.CellEditorUtils
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.common.compose.St
import javax.inject.Inject


data class CellEditorStateImp @Inject constructor(
    @CellEditorInitCursorIdSt
    override val targetCursorIdSt: St<@JvmSuppressWildcards CursorStateId>?,
    @FalseMs
    override val isActiveMs: Ms<Boolean>,
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
) : CellEditorState {
    companion object {
        fun defaultForTest(): CellEditorStateImp {
            return CellEditorStateImp(null, false.toMs())
        }
    }

    override fun stopGettingRangeAddress(): CellEditorState {
        val rt = this.moveTextFromRangeSelectorTextToCurrentText()
        return rt
    }

    private fun moveTextFromRangeSelectorTextToCurrentText(): CellEditorState {
        if (rangeSelectorTextField != null) {
            return this.setCurrentText(rangeSelectorTextField.text).setRangeSelectorText(null)
        } else {
            return this
        }
    }

    @False
    override val allowRangeSelector: Boolean
        get() {
            return CellEditorUtils.allowSelector(this.currentText)
        }

    override var isActive: Boolean by isActiveMs
    override val isActiveAndAllowRangeSelector: Boolean
        get() = isActive && allowRangeSelector
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
            if (this.isActive) {
                val rst:TextFieldValue = this.rangeSelectorTextField ?: return this.currentTextField
                return rst
            }else{
                return this.currentTextField
            }
        }
    override val displayText: String
        get() = displayTextField.text
    override val rangeSelectorText: String?
        get() = rangeSelectorTextField?.text

    override fun setRangeSelectorText(newTextField: TextFieldValue?): CellEditorState {
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

    override fun clearAllText(): CellEditorState {
        return this.setCurrentText("").setRangeSelectorText(null)
    }

    /**
     * Open this cell editor at the cursor whose id is [cursorIdMs]
     */
    override fun open(cursorIdMs: St<CursorStateId>): CellEditorState {
        isActiveMs.value = true
        return this.copy(targetCursorIdSt = cursorIdMs, rangeSelectorCursorIdSt = cursorIdMs)
    }

    override fun close(): CellEditorState {
        isActiveMs.value = false
        return this.copy(targetCursorIdSt = null, targetCell = null)
            .stopGettingRangeAddress()
            .setRangeSelectorText(null)
    }
}
