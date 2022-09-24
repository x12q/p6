package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

abstract class BaseSlider : GridSlider {
    override val currentDisplayedRange: RangeAddress
        get() = RangeAddress(visibleColRange,visibleRowRange)
    override val lastVisibleColNotMargin: Int
        get() = if (marginCol != null) {
            maxOf(lastVisibleCol - 1, 0)
//            lastVisibleCol
        } else {
            lastVisibleCol
        }

    override val lastVisibleRowNotMargin: Int
        get() {
            return if (marginRow != null) {
                maxOf(lastVisibleRow - 1, 0)
            } else {
                lastVisibleRow
            }
        }

    override fun containAddressNotMargin(cellAddress: CellAddress): Boolean {
        return cellAddress.colIndex in visibleColRangeExcludeMargin && cellAddress.rowIndex in visibleRowRangeExcludeMargin
    }

    override val visibleColRangeExcludeMargin: IntRange
        get() {
            return if (marginCol != null) {
                firstVisibleCol..maxOf(firstVisibleCol, marginCol!! - 1)
            } else {
                visibleColRange
            }
        }

    override val visibleRowRangeExcludeMargin: IntRange
        get() {
            return if (marginRow != null) {
                firstVisibleRow..maxOf(firstVisibleRow, marginRow!! - 1)
            } else {
                visibleRowRange
            }
        }

    override fun followCursor(newCursorState: CursorState): GridSlider {
        val slider = this
        // move slider with cursor
        val newMainCell: CellAddress = newCursorState.mainCell
//        if (!slider.containAddress(newMainCell)) {
        if (!slider.containAddressNotMargin(newMainCell)) {
            // x: cursor in the far right
            val maxColDif = newMainCell.colIndex - slider.lastVisibleColNotMargin
            if (maxColDif > 0) {
                return slider.shiftRight(maxColDif)
            } else {
                // x: cursor in the far left
                val minColDif = slider.firstVisibleCol - newMainCell.colIndex
                if (minColDif > 0) {
                    return slider.shiftLeft(minColDif)
                }
            }

//            val maxRowDif = newMainCell.rowIndex - slider.lastVisibleRow
            val maxRowDif = newMainCell.rowIndex - slider.lastVisibleRowNotMargin
            if (maxRowDif > 0) {
                // x: cursor at bot
                return slider.shiftDown(maxRowDif)
            } else {
                // x: cursor at top
                val minRowDif = slider.firstVisibleRow - newMainCell.rowIndex
                if (minRowDif > 0) {
                    return slider.shiftUp(minRowDif)
                }
            }
            return this
        } else {
            return this
        }
    }
}
