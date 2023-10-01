package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState

/**
 * A slider contains a range of row and a range of col.
 * A slider can shift its ranges up, down, left, right.
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
     * the last row of the row range used by vertical edge slider
     */
    val edgeSliderRow:Int

    /**
     * This is this range: (1,[edgeSliderRow]).
     * This range is always larger than the [visibleRowRangeIncludeMargin] and is expanded when [visibleRowRangeIncludeMargin] reach this limit.
     */
    val edgeSliderRowRange:IntRange

    /**
     * the last col of the col range used by vertical edge slider
     */
    val edgeSliderCol:Int

    /**
     * This is this range: (1,[edgeSliderCol])
     * This range is always larger than the [visibleColRangeIncludeMargin] and is expanded whenever [visibleColRangeIncludeMargin] reach this limit.
     */
    val edgeSliderColRange:IntRange

    val currentDisplayedRange:RangeAddress

    /**
     * The top-left of a grid. This is the root of the slider
     */
    val topLeftCell: CellAddress

    val firstVisibleCol: Int
    val lastVisibleCol: Int

    val visibleColRangeIncludeMargin: IntRange
    val visibleColRangeExcludeMargin: IntRange
    val lastVisibleColNotMargin: Int
    fun setVisibleColRange(i: IntRange): GridSlider

    val firstVisibleRow: Int
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
}
