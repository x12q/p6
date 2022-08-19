package com.emeraldblast.p6.ui.document.worksheet.cursor.state

import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.document.worksheet.state.RangeConstraint

/**
 * State of cell cursor
 */
interface CursorState : WbWsSt {
    val idMs:Ms<CursorStateId>
    var id:CursorStateId

    /**
     * clipboard range indicates the range that was just copied into the clipboard
     */
    val clipboardRange: RangeAddress
    fun setClipboardRange(rangeAddress: RangeAddress):CursorState
    fun removeClipboardRange():CursorState
    fun containInClipboard(cellAddress: CellAddress):Boolean

    val colFromFragCells: List<Int>
    val colFromRange: List<IntRange>

    val maxCol:Int?
    val minCol:Int?

    /**
     * All fragmented range + selected range
     */
    @Deprecated("use the prop instead")
    fun allRanges():List<RangeAddress>
    val allRanges: List<RangeAddress>

    /**
     * All fragmented cell + anchor cell
     */
    @Deprecated("use the prop instead")
    fun allFragCells():List<CellAddress>
    val allFragCells: List<CellAddress>

    val cellEditorStateMs:Ms<CellEditorState>
    val cellEditorState: CellEditorState get() = cellEditorStateMs.value
    val isEditingMs:St<Boolean>
    val isEditing: Boolean

    /**
     * this is the range that the cursor is permitted to navigate
     */
    val rangeConstraint: RangeConstraint


    val mainCell: CellAddress

    /**
     * main range is for shift+arrow selection
     */
    val mainRange: RangeAddress?

    /**
     * fragmented cell set, not including the main cell
     */
    val fragmentedCells: Set<CellAddress>

    /**
     * fragmented range set, not including the main range
     */
    val fragmentedRanges: Set<RangeAddress>

    /**
     * move the cursor up 1 row
     */
    fun up(): CursorState

    /**
     * move the cursor down 1 row
     */
    fun down(): CursorState

    /**
     * move the cursor left 1 col
     */
    fun left(): CursorState

    /**
     * move the cursor right 1 col
     */
    fun right(): CursorState

    /**
     * point the cursor to a new cell
     */
    fun setMainCell(newCellAddress: CellAddress): CursorState


    /**
     * point the cursor to a new range
     */
    fun setMainRange(rangeAddress: RangeAddress?): CursorState


    fun setFragmentedCells(cells: Collection<CellAddress>): CursorState

    /**
     * A cursor is within a col bound, row bound iff all of its selection cells are within such bounds
     */
    fun isWithinLimit(colBound: IntRange, rowBound: IntRange): Boolean

    /**
     * check if a cell address is in the selection of this cursor
     */
    fun isPointingTo(address: CellAddress): Boolean

    /**
     * make this cursor select a whole col
     */
    fun selectWholeCol(colIndex: Int): CursorState

    /**
     * make this cursor select a whole row
     */
    fun selectWholeRow(rowIndex: Int): CursorState

    /**
     * a cell to this cursor's fragment selections
     */
    fun addFragCell(cellAddress: CellAddress): CursorState
    fun addFragCells(cellAddressList: Collection<CellAddress>):CursorState
    /**
     * remove a cell from this cursor's fragment selections
     */
    fun removeFragCell(cellAddress: CellAddress): CursorState

    /**
     * remove all cells in fragment selections
     */
    fun removeAllFragmentedCells(): CursorState

    /**
     * remove range selection
     */
    fun removeMainRange(): CursorState

    fun addFragRange(rangeAddress: RangeAddress): CursorState
    fun addFragRanges(rangeAddressList: Collection<RangeAddress>): CursorState
    fun removeFragRange(rangeAddress: RangeAddress): CursorState
    fun setFragRanges(ranges:Collection<RangeAddress>): CursorState
    fun removeAllSelectedFragRange(): CursorState

    fun removeAllExceptAnchorCell(): CursorState

    val rowFromFragCells: List<Int>
    val rowFromRange: List<IntRange>
    val maxRow: Int?
    val minRow: Int?

    /**
     * Attempt to merge everything this cursor pointing to into one range
     */
    fun mergeAllIntoOne(): RangeAddress?
    fun attemptToMergeAllIntoOne(): CursorState
}
