package com.qxdzbc.p6.ui.document.worksheet.cursor.actions

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardAction
import com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event.HandleCursorKeyboardEventAction
import com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor.PasteRangeToCursor
import com.qxdzbc.p6.app.action.cursor.undo_on_cursor.UndoOnCursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action.ThumbAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction

interface CursorAction :
    SelectWholeColumnForAllSelectedCellAction,
    SelectWholeRowForAllSelectedCellAction,
    HandleCursorKeyboardEventAction,
    PasteRangeToCursor,
    CopyCursorRangeToClipboardAction,
    UndoOnCursorAction
{
    val cellEditorAction: CellEditorAction
    val thumbAction: ThumbAction

    fun moveCursorTo(wbws: WbWs, cellLabel: String)

    fun getFormulaRangeAndColor(wbws: WbWs): Map<RangeAddress, Color>

    fun focusOnCursor(cursorId: CursorStateId)
    fun freeFocusOnCursor(cursorId: CursorStateId)
    fun updateCursorFocus(cursorId: CursorStateId, focused: Boolean)
//    fun updateCellEditorFocus(cursorId: CursorStateId,focused: Boolean)
}

