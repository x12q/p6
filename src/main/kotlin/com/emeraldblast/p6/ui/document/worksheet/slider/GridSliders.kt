package com.emeraldblast.p6.ui.document.worksheet.slider

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.common.R



object GridSliders {
    fun default(): GridSlider {
        return create()
    }

    fun create(
        visibleColRange: IntRange = R.worksheetValue.defaultVisibleColRange,
        visibleRowRange: IntRange = R.worksheetValue.defaultVisibleRowRange,
        colLimit: IntRange = R.worksheetValue.defaultColRange,
        rowLimit: IntRange = R.worksheetValue.defaultRowRange,
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
