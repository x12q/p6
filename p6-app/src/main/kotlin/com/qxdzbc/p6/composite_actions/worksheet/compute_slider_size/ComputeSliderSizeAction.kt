package com.qxdzbc.p6.composite_actions.worksheet.compute_slider_size

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState

interface ComputeSliderSizeAction {

    /**
     * Compute the slider's properties from the currently available space.
     */
    fun computeSliderPropertiesForAvailableSpace(
        wsState: WorksheetState,
        availableSpace: DpSize,
    )
}
