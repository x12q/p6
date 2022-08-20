package com.emeraldblast.p6.ui.document.worksheet.slider

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState

abstract class BaseSlider:GridSlider {
    override fun followCursor(newCursorState: CursorState): GridSlider {
        val slider = this
        // move slider with cursor
        val newMainCell: CellAddress = newCursorState.mainCell
        if (!slider.containAddress(newMainCell)) {
            // x: cursor in the far right
            val maxColDif = newMainCell.colIndex - slider.lastVisibleCol
            if (maxColDif > 0) {
                return slider.shiftRight(maxColDif)
            } else {
                // x: cursor in the far left
                val minColDif = slider.firstVisibleCol - newMainCell.colIndex
                if (minColDif > 0) {
                    return slider.shiftLeft(minColDif)
                }
            }

            val maxRowDif = newMainCell.rowIndex - slider.lastVisibleRow
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
        }else{
            return this
        }
    }
}
