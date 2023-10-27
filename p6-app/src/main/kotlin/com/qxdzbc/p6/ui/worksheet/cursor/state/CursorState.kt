package com.qxdzbc.p6.ui.worksheet.cursor.state

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.worksheet.cursor.thumb.state.ThumbState
import com.qxdzbc.p6.ui.worksheet.state.RangeConstraint

/**
 * State of a cell cursor
 */
interface CursorState : WbWsSt {
    val idMs:Ms<CursorId>
    var id:CursorId

    val cellLayoutCoorsMapSt:St<Map<CellAddress, P6Layout>>

    val cellLayoutCoorsMap: Map<CellAddress, P6Layout>

    val thumbStateMs:Ms<ThumbState>

    var thumbState: ThumbState

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
    val allRanges: List<RangeAddress>

    /**
     * All fragmented cell + anchor cell
     */
    val allFragCells: List<CellAddress>

    val cellEditorStateMs:Ms<CellEditorState>
    val cellEditorState: CellEditorState get() = cellEditorStateMs.value
    val isEditingMs:St<Boolean>
    val isEditing: Boolean

    /**
     * this is the range that the cursor is permitted to navigate
     */
    val rangeConstraint: RangeConstraint

    val mainCellSt:St<CellAddress>
    val mainCell: CellAddress

    val mainCellId:CellId

    /**
     * Construct a string representing this cursor main selection address. Prioritize main range over main cell.
     *
     * Eg: "A1:B3@Sheet1@Wbkey2"
     */
    fun mainSelectionStr(against:CursorId):String
    /**
     * point the cursor to a new cell
     */
    fun setMainCell(newCellAddress: CellAddress): CursorState

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

    fun removeAllExceptMainCell(): CursorState

    val rowFromFragCells: List<Int>
    val rowFromRange: List<IntRange>
    val maxRow: Int?
    val minRow: Int?

    /**
     * Attempt to merge everything this cursor pointing to into one range
     */
    fun mergeAllIntoOne(): RangeAddress?
    fun attemptToMergeAllRangeIntoOne(): CursorState

    /**
     * true if this cursor contains either fragment cell or fragment range
     */
    val containFragment:Boolean
}
