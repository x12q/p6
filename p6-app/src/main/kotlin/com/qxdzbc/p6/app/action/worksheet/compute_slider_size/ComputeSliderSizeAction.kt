package com.qxdzbc.p6.app.action.worksheet.compute_slider_size

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

interface ComputeSliderSizeAction {
    /**
     * Compute the slider from the currently available space
     */
    fun computeSliderSize(wsLoc: WbWsSt)
}
