package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState

interface ThumbState {

    val size:DpSize
    val offsetNegate:DpSize

    val cursorId: CursorStateId
    val mainCell:CellAddress

    val cellLayoutCoorMap: Map<CellAddress, LayoutCoorWrapper>

    val isShowingSelectedRange:Boolean

    val selectedRangeSizeOrZero:DpSize
    val selectedRangeSize:DpSize?
    val selectedRangeWindowOffsetOrZero:Offset
    val selectedRangeWindowOffset:Offset?


    val selectRectState: SelectRectState
    fun setSelectRectState(i:SelectRectState): ThumbState

    /**
     * return a pair of [CellAddress] denoting the top and bottom cell in the current selected range.
     */
    fun getTopBotCells(): Pair<CellAddress, CellAddress>?

    /**
     * Return a pair [CellAddress] denoting the starting cell and ending cell.
     * Starting cell is the cell where the drag action start from, this is always the main cell.
     * Ending cell is where the drag action end. Ending cell could be above or below the starting cell.
     */
    fun getStartEndCells():Pair<CellAddress, CellAddress>
}
