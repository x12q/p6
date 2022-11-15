package com.qxdzbc.p6.ui.app.cell_editor.state

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import org.antlr.v4.runtime.tree.ParseTree

interface CellEditorState {
    val rangeSelectorAllowState:RangeSelectorAllowState
    val parseTreeMs:Ms<ParseTree?>
    val parseTree: ParseTree?
    fun setParseTree(i: ParseTree?): CellEditorState

    /**
     * return this cell ceditor to empty state, in which:
     *  - empty text
     *  - null parse tree
     */
    fun clearAll():CellEditorState

    /**
     * move [rangeSelectorTextField]'s content to [currentTextField], then nullify [rangeSelectorTextField]
     */
    fun stopGettingRangeAddress(): CellEditorState

    /**
     * A cell editor allows range selector to be used when the last character of the formula is one of the characters in [com.qxdzbc.p6.ui.app.cell_editor.CellEditorUtils.activationChars]
     */
    val allowRangeSelector: Boolean

    val rangeSelectorCursorIdSt: St<CursorStateId>?
    val rangeSelectorCursorId: CursorStateId?
    fun setRangeSelectorCursorId(i: St<CursorStateId>?): CellEditorState

    val rangeSelectorIsSameAsTargetCursor: Boolean
        get() = rangeSelectorCursorId?.let { rs ->
            targetCursorId?.let {
                it.isSameWbWs(rs)
            }
        } ?: false

    /**
     * This is for locating the edit target.
     * Which workbook, worksheet does the target cursor belong to?
     */
    val targetCursorIdSt: St<CursorStateId>?
    val targetCursorId: CursorStateId?

    /**
     * This is the address of the cell being edited by this cell editor
     */
    val targetCell: CellAddress?
    fun setTargetCell(newCellAddress: CellAddress?): CellEditorState

    val targetWbKey: WorkbookKey? get() = targetCursorId?.wbKey
    val targetWsName: String? get() = targetCursorId?.wsName

    val displayTextField: TextFieldValue
    val displayText: String

    /**
     * A temporary text field that stores a temporary text when users are using the cursor at [rangeSelectorCursorId] to select a cell/range. During this selection time, [rangeSelectorTextField] is displayed on the view instead of [currentTextField].
     *
     * [rangeSelectorTextField] = [currentTextField] + selected range address.
     *
     * The content of [rangeSelectorTextField] will be copied into [currentTextField] when the range selector is done with its work, then it will be nullified. This is done by function [stopGettingRangeAddress]
     */
    val rangeSelectorTextField: TextFieldValue?
    val rangeSelectorText: String?
    fun setRangeSelectorTextField(newTextField: TextFieldValue?): CellEditorState

    /**
     * current text is the one that hold the formula that will be run at the end
     */
    val currentText: String
    val currentTextField: TextFieldValue
    fun setCurrentText(newText: String): CellEditorState
    fun setCurrentTextField(newTextField: TextFieldValue): CellEditorState

    /**
     * if this cell editor is active and allow range selector, update the [rangeSelectorTextField], otherwise update the [currentTextField]
     */
    fun setDisplayTextField(newTextField: TextFieldValue):CellEditorState

    /**
     * clear both current text and range selector text
     */
    fun clearAllText(): CellEditorState

    /**
     * A cell editor is active when it is being opened
     */
    val isOpenMs: Ms<Boolean>
    val isOpen: Boolean
    val isNotOpen:Boolean


    val isActiveAndAllowRangeSelector:Boolean

    /**
     * open cell editor at cursor denoted by [cursorIdMs]
     */
    fun open(cursorIdMs: St<CursorStateId>): CellEditorState

    /**
     * switch this state to close state
     */
    fun close(): CellEditorState
}
