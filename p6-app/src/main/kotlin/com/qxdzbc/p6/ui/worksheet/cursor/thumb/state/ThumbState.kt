package com.qxdzbc.p6.ui.worksheet.cursor.thumb.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.worksheet.select_rect.SelectRectState

interface ThumbState {

    val size:DpSize

    /**
     * a fixed value for adjust the position of the thumb on the view.
     */
    val offsetNegate:DpSize

    val cursorId: CursorId
    val mainCell:CellAddress

    val cellLayoutCoorMap: Map<CellAddress, LayoutCoorWrapper>

    val isShowingSelectedRange:Boolean

    val selectedRangeSizeOrZero:Size
    val selectedRangeSize:Size?
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
     * Starting cell is the cell where the drag action start from, this is always the [mainCell].
     * Ending cell is where the drag action end. Ending cell could be above or below the starting cell.
     */
    fun getStartEndCells():Pair<CellAddress, CellAddress>

    /**
     * Range that is currently selected by user dragging the thumb.
     * A null value indicate that there is no range being selected.
     */
    val selectedRange: RangeAddress?
}
