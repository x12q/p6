package com.qxdzbc.p6.composite_actions.worksheet.compute_slider_size

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState

interface ComputeSliderSizeAction {

//    fun computeSliderSize(
//        oldGridSlider: GridSlider,
//        sizeConstraint: DpSize,
//        anchorCell: CellAddress,
//        getColWidth: (colIndex: Int) -> Dp,
//        getRowHeight: (rowIndex: Int) -> Dp
//    ): GridSlider

    /**
     * Compute the slider's properties from the currently available space. These are:
     * - both pixel/dp size
     * - row range and col range to display
     */
    fun computeSliderProperties(
        availableSpace: IntSize,
        wsState: WorksheetState,
        density: Density
    )
}
