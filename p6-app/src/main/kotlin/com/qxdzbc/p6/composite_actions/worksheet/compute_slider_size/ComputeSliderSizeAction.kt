package com.qxdzbc.p6.composite_actions.worksheet.compute_slider_size

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider

interface ComputeSliderSizeAction {
    /**
     * Compute the slider from the currently available space
     */
    fun computeSliderSize(wsLoc: WbWsSt,density:Density)
    fun computeSliderSize(
        oldGridSlider: GridSlider,
        sizeConstraint: DpSize,
        anchorCell: CellAddress,
        colWidthGetter: (colIndex: Int) -> Dp,
        rowHeightGetter: (rowIndex: Int) -> Dp
    ): GridSlider
}
