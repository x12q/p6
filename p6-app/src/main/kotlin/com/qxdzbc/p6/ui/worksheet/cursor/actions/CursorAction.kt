package com.qxdzbc.p6.ui.worksheet.cursor.actions

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.composite_actions.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardAction
import com.qxdzbc.p6.composite_actions.cursor.handle_cursor_keyboard_event.HandleKeyboardEventOnWsCursor
import com.qxdzbc.p6.composite_actions.cursor.paste_range_to_cursor.PasteRangeToCursor
import com.qxdzbc.p6.composite_actions.cursor.undo_redo.UndoRedoAction
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.worksheet.cursor.thumb.action.ThumbAction
import com.qxdzbc.p6.ui.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction

interface CursorAction :
    SelectWholeColumnForAllSelectedCellAction,
    SelectWholeRowForAllSelectedCellAction,
    HandleKeyboardEventOnWsCursor,
    PasteRangeToCursor,
    CopyCursorRangeToClipboardAction,
    UndoRedoAction
{
    val cellEditorAction: CellEditorAction

    val thumbAction: ThumbAction

    fun moveCursorTo(wbws: WbWs, cellLabel: String)

    fun getFormulaRangeAndColor(wbws: WbWs): Map<RangeAddress, Color>

    fun focusOnCursor(cursorId: CursorId)

    fun freeFocusOnCursor(cursorId: CursorId)

    fun updateCursorFocus(cursorId: CursorId, focused: Boolean)
}

