package com.qxdzbc.p6.app.action.worksheet.compute_slider_size

import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.toSize
import com.qxdzbc.common.compose.SizeUtils.toDpSize
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ComputeSliderSizeActionImp @Inject constructor(
    val stateContMs: St<@JvmSuppressWildcards StateContainer>,
): ComputeSliderSizeAction {

    private val stateCont by stateContMs
    override fun computeSliderSize(wsLoc: WbWsSt,density: Density) {
        stateCont.getWsState(wsLoc)?.also { wsState ->
            val currentSlider = wsState.slider
            val sizeConstraint = wsState.cellGridLayoutCoorWrapper?.pixelSizeOrZero
            val newSlider = if (sizeConstraint != null) {
                computeSliderSize(
                    currentSlider, sizeConstraint.toDpSize(density), wsState.slider.topLeftCell,
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

    /**
     * Compute grid slide size by looping through displayed cells,
     */
    override fun computeSliderSize(
        oldGridSlider: GridSlider,
        sizeConstraint: DpSize,
        anchorCell: CellAddress,
        colWidthGetter: (colIndex: Int) -> Int,
        rowHeightGetter: (rowIndex: Int) -> Int,
    ): GridSlider {
        val limitWidth = sizeConstraint.width.value
        val limitHeight = sizeConstraint.height.value

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
