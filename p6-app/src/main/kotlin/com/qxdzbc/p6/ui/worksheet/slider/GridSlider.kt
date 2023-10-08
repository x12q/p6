package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState

/**
 * A slider contains a range of row and a range of col.
 * A slider can shift its ranges up, down, left, right.
 * TODO currently grid slider is presented as a Ms<>. Make it a non-Ms
 */
interface GridSlider {

    /**
     * The limit of column, in which the slider can move around
     */
    val colLimit: IntRange

    /**
     * The limit of row, in which the slider can move around
     */
    val rowLimit: IntRange

    /**
     * the last row of the row range used by vertical scroll bar, this is not in the visible row range
     */
    val scrollBarLastRow:Int

    /**
     * This is this range: (1,[scrollBarLastRow]).
     * This range is always larger than the [visibleRowRangeIncludeMargin] and is expanded when [visibleRowRangeIncludeMargin] reach this limit.
     */
    val scrollBarRowRange:IntRange

    /**
     * the last col of the col range used by vertical scroll bar, this is not in the visible col range
     */
    val scrollBarLastCol:Int

    /**
     * This is this range: (1,[scrollBarLastCol])
     * This range is always larger than the [visibleColRangeIncludeMargin] and is expanded whenever [visibleColRangeIncludeMargin] reach this limit.
     */
    val scrollBarColRange:IntRange

    val currentDisplayedRange:RangeAddress

    /**
     * The top-left of a grid. This is the root of the slider
     */
    val topLeftCell: CellAddress

    val firstVisibleCol: Int
    /**
     * last visible row is the last col to be seen on screen, it can be the margin col
     */
    val lastVisibleCol: Int

    val visibleColRangeIncludeMargin: IntRange
    val visibleColRangeExcludeMargin: IntRange
    val lastVisibleColNotMargin: Int
    fun setVisibleColRange(i: IntRange): GridSlider

    val firstVisibleRow: Int

    /**
     * last visible row is the last row to be seen on screen, it can be the margin row
     */
    val lastVisibleRow: Int
    val visibleRowRangeIncludeMargin: IntRange
    val visibleRowRangeExcludeMargin: IntRange

    /**
     * Margin row is the row at the margin of the sheet. It is only shown partially
     */
    val marginRow: Int?
    fun setMarginRow(i: Int?): GridSlider

    /**
     * Margin col is the col at the margin of the sheet. It is only shown partially
     */
    val marginCol: Int?
    fun setMarginCol(i: Int?): GridSlider

    fun setVisibleRowRange(i: IntRange): GridSlider

    fun containColInVisibleRange(col: Int): Boolean {
        return col in visibleColRangeIncludeMargin
    }

    fun containRowInVisibleRange(row: Int): Boolean {
        return row in visibleRowRangeIncludeMargin
    }

    fun containAddressInVisibleRange(cellAddress: CellAddress): Boolean

    fun containAddressNotMargin(cellAddress: CellAddress): Boolean

    fun containAddressInVisibleRange(col: Int, row: Int): Boolean

    /**
     * move the slider to the left by [colCount] unit
     * @return a new slider
     */
    fun shiftLeft(colCount: Int): GridSlider

    /**
     * move the slider to the right by [colCount] unit
     * @return a new slider
     */
    fun shiftRight(colCount: Int): GridSlider

    /**
     * move the slider a number ([rowCount]) of cells up
     * @return a new slider
     */
    fun shiftUp(rowCount: Int): GridSlider

    /**
     * move the slider a number ([rowCount]) of row down
     * @return a new slider
     */
    fun shiftDown(rowCount: Int): GridSlider

    /**
     * move slider in relative to a cursor's main cell
     * @return a new slider, or itself if the slider does not move
     */
    fun followCursorMainCell(cursorState: CursorState): GridSlider

    /**
     * Follow the bot-right cell of a cursor main range
     */
    fun followCursorMainRangeBotRight(cursorState: CursorState): GridSlider
    /**
     * Follow a cell, if it is out of this gridslider, produce a new slider that contain that cell on the appropriate edge
     */
    fun followCell(cellAddress: CellAddress):GridSlider
    val lastVisibleRowNotMargin: Int

    /**
     * expand bar limit if needed. If the limits are to increase, increase them by [margin]
     */
    fun expandScrollBarLimitIfNeed(margin: Int = GridSliderConstants.edgeAdditionItemCount): GridSlider
    /**
     * shrink bar limit if the current limit is too large. If the limits are to increase, increase them by [margin]
     */
    fun shrinkScrollBarLimitIfNeed(shrinkTo:Int = GridSliderConstants.edgeAdditionItemCount): GridSlider

    /**
     * Reset the scroll bar limit to the default value
     */
    fun resetScrollBarLimit(): GridSlider

    /**
     * Compute the percentage of row that has been scrolled over compare to the [scrollBarRowRange]
     */
    fun computeScrolledRowPercentage():Float

    /**
     * Compute the percentage of columns that has been scrolled over compare to the [scrollBarRowRange]
     */
    fun computeScrolledColPercentage():Float

}
