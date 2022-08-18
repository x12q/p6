package com.emeraldblast.p6.ui.app.cell_editor.in_cell.state

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.di.DefaultTextFieldValue
import com.emeraldblast.p6.di.False
import com.emeraldblast.p6.di.FalseMs
import com.emeraldblast.p6.di.state.app_state.CellEditorInitCursorIdSt
import com.emeraldblast.p6.di.state.app_state.DefaultNullCellAddress
import com.emeraldblast.p6.di.state.app_state.NullTextFieldValue
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.CellEditorUtils
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.emeraldblast.p6.ui.common.compose.St
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
    override val rangeSelectorText: TextFieldValue? = null,
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
        if (rangeSelectorText != null) {
            return this.setCurrentText(rangeSelectorText.text).setRangeSelectorText(null)
        } else {
            return this
        }
    }

    @False
    override val allowRangeSelector: Boolean
        get() {
            return CellEditorUtils.allowSelector(this.currentText)
        }

    override val isActive: Boolean by isActiveMs
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

    override val displayText: TextFieldValue
        get() {
            if (this.isActive) {
                if (this.rangeSelectorText != null) {
                    return this.rangeSelectorText
                }
            }
            return this.currentTextField
        }

    override fun setRangeSelectorText(newTextField: TextFieldValue?): CellEditorState {
        return this.copy(rangeSelectorText = newTextField)
    }

    /**
     * TODO fix it so that the text cursor is at the end of the new text
     */
    override fun setCurrentText(newText: String): CellEditorState {
        return this.copy(
            currentTextField = this.currentTextField
                .copy(text = newText)
        )
        // the code below will crash the app
        //            .copy(text = newText,selection=TextRange(newText.length)))
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
