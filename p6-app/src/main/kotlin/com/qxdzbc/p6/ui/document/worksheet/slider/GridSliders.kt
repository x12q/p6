package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.ui.common.P6R



object GridSliders {
    fun default(): GridSlider {
        return create()
    }

    fun create(
        visibleColRange: IntRange = P6R.worksheetValue.defaultVisibleColRange,
        visibleRowRange: IntRange = P6R.worksheetValue.defaultVisibleRowRange,
        colLimit: IntRange = P6R.worksheetValue.defaultColRange,
        rowLimit: IntRange = P6R.worksheetValue.defaultRowRange,
    ): GridSlider {
        return LimitedSlider(
            slider = GridSliderImp(
                visibleColRange = visibleColRange,
                visibleRowRange = visibleRowRange,
            ),
            colLimit = colLimit,
            rowLimit = rowLimit
        )
    }

    fun small(): GridSlider {
        return create(colLimit = 1..15, rowLimit = 1..15)
    }
}
