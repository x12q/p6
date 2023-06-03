package com.qxdzbc.p6.app.action.worksheet.compute_slider_size

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider

interface ComputeSliderSizeAction {
    /**
     * Compute the slider from the currently available space
     */
    fun computeSliderSize(wsLoc: WbWsSt,density:Density)
    fun computeSliderSize(
        oldGridSlider: GridSlider,
        sizeConstraint: DpSize,
        anchorCell: CellAddress,
        colWidthGetter: (colIndex: Int) -> Int,
        rowHeightGetter: (rowIndex: Int) -> Int
    ): GridSlider
}
