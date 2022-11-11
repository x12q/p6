package com.qxdzbc.p6.ui.document.worksheet.cursor.actions

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction


interface CursorAction : SelectWholeColumnForAllSelectedCellAction, SelectWholeRowForAllSelectedCellAction {

    /**
     * f2 key
     */
    fun f2(wbws: WbWs)
    fun home(wbws: WbWs)
    fun end(wbws: WbWs)
    fun ctrlUp(wbws: WbWs)
    fun ctrlDown(wbws: WbWs)
    fun ctrlRight(wbws: WbWs)
    fun ctrlShiftLeft(wbws: WbWs)
    fun ctrlShiftRight(wbws: WbWs)
    fun ctrlShiftUp(wbws: WbWs)
    fun ctrlShiftDown(wbws: WbWs)
    fun ctrlLeft(wbws: WbWs)
    fun up(wbws: WbWs)
    fun down(wbws: WbWs)
    fun left(wbws: WbWs)
    fun right(wbws: WbWs)
    fun moveCursorTo(wbws: WbWs,cellLabel:String)
    fun shiftUp(wbws: WbWs)
    fun shiftDown(wbws: WbWs)
    fun shiftLeft(wbws: WbWs)
    fun shiftRight(wbws: WbWs)

    fun onDeleteKey(wbws: WbWs)
    fun undo(wbws: WbWs)
    fun handleKeyboardEvent(keyEvent: P6KeyEvent, wbws: WbWs): Boolean
    fun pasteRange(wbws: WbWs)
    fun rangeToClipboard(wbws: WbWs)

    fun getFormulaRangeAndColor(wbws: WbWs):Map<RangeAddress,Color>

    fun focusOnCursor(cursorId: CursorStateId)
    fun freeFocusOnCursor(cursorId: CursorStateId)
    fun updateCursorFocus(cursorId: CursorStateId,focused: Boolean)
//    fun updateCellEditorFocus(cursorId: CursorStateId,focused: Boolean)
}

