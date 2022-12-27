package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment

interface UpdateCellFormatAction {
    fun setCellBackgroundColor(cellId: CellId, color: Color?,undo:Boolean=true)
    fun setBackgroundColorOnSelectedCells(color: Color?,undo:Boolean=true)

    fun setCellTextSize(cellId: CellId, textSize:Float?,undo:Boolean=true)
    /**
     * set text size of all cells selected by the current cursor to [textSize]
     */
    fun setSelectedCellsTextSize(textSize:Float?,undo:Boolean=true)

    fun setCellTextColor(cellId: CellId, color:Color?,undo:Boolean=true)
    fun setTextColorOnSelectedCells(color: Color?,undo:Boolean=true)

    fun setCellTextUnderlined(cellId: CellId, underlined:Boolean?,undo:Boolean=true)
    fun setUnderlinedOnSelectedCells(underlined:Boolean?,undo:Boolean=true)

    fun setCellTextCrossed(cellId: CellId, crossed:Boolean?,undo:Boolean=true)
    fun setCrossedOnSelectedCell(cellId: CellId, crossed:Boolean?,undo:Boolean=true)

    fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment?,undo:Boolean=true)
    fun setHorizontalAlignmentOnSelectedCells(cellId: CellId, alignment: TextHorizontalAlignment?,undo:Boolean=true)

    fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment?,undo:Boolean=true)
    fun setVerticalAlignmentOnSelectedCells(cellId: CellId, alignment: TextVerticalAlignment?,undo:Boolean=true)
}
