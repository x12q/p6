package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment

interface UpdateCellFormatAction {
    fun setCellBackgroundColor(cellId: CellId, color: Color?)
    fun setBackgroundColorOnSelectedCells(color: Color?)

    fun setCellTextSize(cellId: CellId, textSize:Float?)
    /**
     * set text size of all cells selected by the current cursor to [textSize]
     */
    fun setSelectedCellsTextSize(textSize:Float?)

    fun setCellTextColor(cellId: CellId, color:Color?)
    fun setTextColorOnSelectedCells(color: Color?)

    fun setCellTextUnderlined(cellId: CellId, underlined:Boolean?)
    fun setUnderlinedOnSelectedCells(underlined:Boolean?)

    fun setCellTextCrossed(cellId: CellId, crossed:Boolean?)
    fun setCrossedOnSelectedCell(cellId: CellId, crossed:Boolean?)

    fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment?)
    fun setHorizontalAlignmentOnSelectedCells(cellId: CellId, alignment: TextHorizontalAlignment?)

    fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment?)
    fun setVerticalAlignmentOnSelectedCells(cellId: CellId, alignment: TextVerticalAlignment?)
}
