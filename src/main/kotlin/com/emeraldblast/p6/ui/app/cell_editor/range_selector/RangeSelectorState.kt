package com.emeraldblast.p6.ui.app.cell_editor.range_selector

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.state.RangeConstraint

interface RangeSelectorState {

    val idMs:Ms<RangeSelectorId>
    var id: RangeSelectorId

    /**
     * this is the range that the selector is permitted to navigate
     */
    val rangeConstraint: RangeConstraint


    /**
     * main range is for shift+arrow selection
     */
    val mainRange: RangeAddress?
    val maxRow: Int?
    val minRow: Int?
    val maxCol:Int?
    val minCol:Int?

    fun contain(address: CellAddress):Boolean

    /**
     * move the selector up 1 row
     */
    fun up(): RangeSelectorState

    /**
     * move the selector down 1 row
     */
    fun down(): RangeSelectorState

    /**
     * move the selector left 1 col
     */
    fun left(): RangeSelectorState

    /**
     * move the selector right 1 col
     */
    fun right(): RangeSelectorState

    /**
     * point the selector to a new cell
     */
    fun setAnchorCell(newCellAddress: CellAddress): RangeSelectorState

    /**
     * point the selector to a new range
     */
    fun setMainRange(rangeAddress: RangeAddress?): RangeSelectorState

    /**
     * check if a cell address is in the selection of this selector
     */
    fun isPointingTo(address: CellAddress): Boolean

    /**
     * make this selector select a whole col
     */
    fun selectWholeCol(colIndex: Int): RangeSelectorState

    /**
     * make this selector select a whole row
     */
    fun selectWholeRow(rowIndex: Int): RangeSelectorState

    /**
     * remove range selection
     */
    fun removeMainRange(): RangeSelectorState
}
