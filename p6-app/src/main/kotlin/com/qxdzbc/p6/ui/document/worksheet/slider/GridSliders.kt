package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants


object GridSliders {
    fun default(): GridSlider {
        return create()
    }

    fun create(
        visibleColRange: IntRange = WorksheetConstants.defaultVisibleColRange,
        visibleRowRange: IntRange = WorksheetConstants.defaultVisibleRowRange,
        colLimit: IntRange = WorksheetConstants.defaultColRange,
        rowLimit: IntRange = WorksheetConstants.defaultRowRange,
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
