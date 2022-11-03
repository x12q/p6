package com.qxdzbc.p6.app.action.worksheet.compute_slider_size

import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import javax.inject.Inject

class ComputeSliderSizeActionImp @Inject constructor(
    val stateContMs: St<@JvmSuppressWildcards StateContainer>,
): ComputeSliderSizeAction {

    private val stateCont by stateContMs
    override fun computeSliderSize(wsLoc: WbWsSt) {
        stateCont.getWsState(wsLoc)?.also { wsState ->
            val currentSlider = wsState.slider
            val availableSize = wsState.cellGridLayoutCoorWrapper?.sizeOrZero
            val newSlider = if (availableSize != null) {
                computeSliderSize(
                    currentSlider, availableSize, wsState.slider.topLeftCell,
                    wsState::getColumnWidthOrDefault,
                    wsState::getRowHeightOrDefault,
                )
            } else {
                null
            }
            if (newSlider != null) {
                wsState.sliderMs.value = newSlider
            }
        }
    }

    override fun computeSliderSize(
        oldGridSlider: GridSlider,
        availableSize: DpSize,
        anchorCell: CellAddress,
        colWidthGetter: (colIndex: Int) -> Int,
        rowHeightGetter: (rowIndex: Int) -> Int,
    ): GridSlider {
        val limitWidth = availableSize.width.value
        val limitHeight = availableSize.height.value

        val fromCol = anchorCell.colIndex
        var toCol = fromCol
        var accumWidth = 0F
        while (accumWidth < limitWidth) {
            accumWidth += colWidthGetter(toCol)
            toCol += 1
        }

        val fromRow = anchorCell.rowIndex
        var toRow = fromRow
        var accumHeight = 0F
        while (accumHeight < limitHeight) {
            accumHeight += rowHeightGetter(toRow)
            toRow += 1
        }
        val lastRow = maxOf(toRow - 1, fromRow)
        val lastCol = maxOf(toCol - 1, fromCol)

        val newSlider = oldGridSlider
            .setVisibleRowRange(fromRow..lastRow)
            .setVisibleColRange(fromCol..lastCol)
            .setMarginRow(if (accumHeight == limitHeight) null else lastRow)
            .setMarginCol(if (accumWidth == limitWidth) null else lastCol)

        return newSlider
    }
}
