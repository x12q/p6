package com.qxdzbc.p6.ui.document.worksheet.cursor.actions

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.common.compose.key_event.PKeyEvent
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction

/**
 * TODO add St alternative to all functions
 */
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
    fun handleKeyboardEvent(keyEvent: PKeyEvent, wbws: WbWs): Boolean
    fun pasteRange(wbws: WbWs)
    fun rangeToClipboard(wbws: WbWs)

    fun getFormulaRangeAndColor(wbws: WbWs):Map<RangeAddress,Color>
}

