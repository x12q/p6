package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

/**
 * A slider contains a range of row and a range of col of fixed width.
 * A slider can shift its ranges up, down, left, right, but cannot change the width of the ranges.
 */
interface GridSlider {

    /**
     * The top-left of a grid. This is the root of the slider
     */
    val topLeftCell: CellAddress

    val firstVisibleCol: Int
    val lastVisibleCol: Int
    val visibleColRange: IntRange
    val visibleColRangeExcludeMargin: IntRange
    val lastVisibleColNotMargin: Int
    fun setVisibleColRange(i: IntRange): GridSlider

    val firstVisibleRow: Int
    val lastVisibleRow: Int
    val visibleRowRange: IntRange
    val visibleRowRangeExcludeMargin: IntRange

    val marginRow: Int?
    fun setMarginRow(i: Int?): GridSlider
    val marginCol: Int?
    fun setMarginCol(i: Int?): GridSlider

    fun setVisibleRowRange(i: IntRange): GridSlider

    fun containCol(col: Int): Boolean {
        return col in visibleColRange
    }

    fun containRow(row: Int): Boolean {
        return row in visibleRowRange
    }

    fun containAddress(cellAddress: CellAddress): Boolean {
        return containCol(cellAddress.colIndex) && containRow(cellAddress.rowIndex)
    }

    fun containAddressNotMargin(cellAddress: CellAddress): Boolean

    fun containAddress(col: Int, row: Int): Boolean {
        return containCol(col) && containRow(row)
    }

    /**
     * move the slider [v] unit to the left, this will decrease the visible column range by v unit
     * @return a new slider
     */
    fun shiftLeft(v: Int): GridSlider

    /**
     * move the slider [v] unit to the right, this will increase the visible column range by v unit
     * @return a new slider
     */
    fun shiftRight(v: Int): GridSlider

    /**
     * move the slider a number ([v]) of cells up, this will decrease the visible row range by v unit
     * @return a new slider
     */
    fun shiftUp(v: Int): GridSlider

    /**
     * move the slider a number ([v]) of cells down, this will increase the visible row range by v unit
     * @return a new slider
     */
    fun shiftDown(v: Int): GridSlider

    /**
     * move slider in relative to a cursor.
     * @return a new slider, or itself if the slider does not move
     */
    fun followCursor(newCursorState: CursorState): GridSlider
    val lastVisibleRowNotMargin: Int
}
