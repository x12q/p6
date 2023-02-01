package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.FormatConfig

interface UpdateCellFormatAction {

    /**
     * apply [config] to worksheet at [wbWsSt]
     */
    fun applyFormatConfig(wbWsSt: WbWsSt, config:FormatConfig, undoable: Boolean)
    fun applyFormatConfig(wbWs: WbWs, config:FormatConfig, undoable: Boolean)

    /**
     * Clear format of cell-format-table at [wbWsSt] respectively. This action uses the ranges of all categories to clear each category. This action is highly destructive and unpredictable. Use with care
     */
    fun clearFormat_Flat(wbWsSt: WbWsSt, config:FormatConfig, undoable: Boolean)

    /**
     * Clear format of cell-format-table at [wbWs] respectively. This action uses ranges of all categories to clear each category. This action is highly destructive and unpredictable. Use with care
     */
    fun clearFormat_Flat(wbWs: WbWs, config:FormatConfig, undoable: Boolean)

    /**
     * Clear format of cell-format-table at [wbWsSt] respectively. This ensures that ranges in each category are used for clear format of that category only.
     */
    fun clearFormat_Respective(wbWsSt: WbWsSt, config:FormatConfig, undoable: Boolean)
    /**
     * Clear format of cell format table at [wbWs] respectively. This ensures that ranges in each category are used for clear format of that category only.
     */
    fun clearFormat_Respective(wbWs: WbWs, config:FormatConfig, undoable: Boolean)

    /**
     * set background of cell at [cellId] to color [color]
     */
    fun setCellBackgroundColor(cellId: CellId, color: Color?, undoable:Boolean)

    /**
     * set background of all cells selected by the active cursor to color [color]
     */
    fun setBackgroundColorOnSelectedCells(color: Color?, undoable:Boolean)

    fun setCellTextSize(cellId: CellId, textSize:Float?, undoable:Boolean)
    /**
     * set text size of all cells selected by the current cursor to [textSize]
     */
    fun setSelectedCellsTextSize(textSize:Float?, undoable:Boolean)

    fun setCellTextColor(cellId: CellId, color:Color?, undoable:Boolean)
    fun setTextColorOnSelectedCells(color: Color?, undoable:Boolean)

    fun setCellTextUnderlined(cellId: CellId, underlined:Boolean?, undoable:Boolean)
    fun setUnderlinedOnSelectedCells(underlined:Boolean?, undoable:Boolean)

    fun setCellTextCrossed(cellId: CellId, crossed:Boolean?, undoable:Boolean)
    fun setCrossedOnSelectedCell(cellId: CellId, crossed:Boolean?, undoable:Boolean)

    fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment?, undoable:Boolean)
    fun setHorizontalAlignmentOnSelectedCells(cellId: CellId, alignment: TextHorizontalAlignment?, undoable:Boolean)

    fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment?, undoable:Boolean)
    fun setVerticalAlignmentOnSelectedCells(cellId: CellId, alignment: TextVerticalAlignment?, undoable:Boolean)
}
