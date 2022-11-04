package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.common.formatter.RangeAddressFormatter
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class MakeCellEditorDisplayTextActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards SubAppStateContainer>,
    val fm: RangeAddressFormatter,
) : MakeCellEditorDisplayTextAction {

    private val stateCont by stateContSt

    override fun makeRangeSelectorText(editorState: CellEditorState): TextFieldValue {
        if (editorState.allowRangeSelector) {
            return makeRangeSelectorText(editorState.currentTextField,editorState.rangeSelectorCursorId,editorState.targetCursorId)
        } else {
            return editorState.currentTextField
        }
    }

    override fun makeRangeSelectorText(
        currentTextField: TextFieldValue,
        selectorId: CursorStateId?,
        cursorId: CursorStateId?
    ): TextFieldValue {
        val rangeSelector: CursorState? = selectorId?.let { stateCont.getCursorState(it) }
        val rsWsName: String? = rangeSelector?.wsName
        val rsRange: RangeAddress? = rangeSelector?.let { getSelectedRange(it) }
        val rsWbKey: WorkbookKey? = rangeSelector?.wbKey

        if (rsWsName != null && rsRange != null && rsWbKey != null) {
            val rangeStr: String = if (rsRange.isCell()) {
                rsRange.topLeft.toRawLabel()
            } else {
                rsRange.rawLabel
            }
            val isSameCursor: Boolean = cursorId == selectorId
            val selectedRangeText: String = if (isSameCursor) {
                rangeStr
            } else {
                val sameWb = rsWbKey == cursorId?.wbKey
                if (sameWb) {
                    fm.formatStr(rangeStr,rsWsName,null,null)
                } else {
                    fm.format(rangeStr,rsWsName,rsWbKey)
                }
            }
            val currentText = currentTextField.text
            val currentSelection = currentTextField.selection

            val preText = currentText.substring(0, currentSelection.start)
            val postText = currentText.substring(currentSelection.end)
            val newText = "${preText}${selectedRangeText}${postText}"

            val newSelection = TextRange(currentSelection.end + selectedRangeText.length)

            return TextFieldValue(text = newText, selection = newSelection)
        } else {
            return currentTextField
        }
    }


    private fun getSelectedRange(cursorState: CursorState): RangeAddress {
        val mainRange = cursorState.mainRange
        if (mainRange != null) {
            return mainRange
        } else {
            val mainCell = cursorState.mainCell
            return RangeAddress(mainCell)
        }
    }
}
