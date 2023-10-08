package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.p6.common.utils.MathUtils
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultVisibleColRange
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultVisibleRowRange
import javax.inject.Inject


/**
 * This class is used inside [GridSliderImp] to handle the col and row shifting aspect of worksheet grid sliders.
 * [visibleRowRange] denoting visible row range.
 * [visibleColRange] denoting visible col range.
 * This class provide functions to shift these two range.
 */
data class ColRowShifter @Inject constructor(
    @DefaultVisibleColRange
    val visibleColRange: IntRange,
    @DefaultVisibleRowRange
    val visibleRowRange: IntRange,
)  {

    val firstVisibleCol: Int get() = visibleColRange.first
    val lastVisibleCol: Int get() = visibleColRange.last

    val firstVisibleRow: Int get() = visibleRowRange.first
    val lastVisibleRow: Int get() = visibleRowRange.last

    fun setVisibleColRange(i: IntRange): ColRowShifter {
        if (i == this.visibleColRange) {
            return this
        } else {
            return this.copy(visibleColRange = i)
        }
    }

    fun setVisibleRowRange(i: IntRange): ColRowShifter {
        if (i == this.visibleRowRange) {
            return this
        } else {
            return this.copy(visibleRowRange = i)
        }
    }

    fun shiftLeft(colCount: Int): ColRowShifter {
        val i = MathUtils.addIntOrDefault(firstVisibleCol, -colCount)
        val w = MathUtils.addIntOrDefault(lastVisibleCol, -colCount)
        return this.copy(
            visibleColRange = IntRange(i, w)
        )
    }

    fun shiftRight(colCount: Int): ColRowShifter {
        return this.shiftLeft(-colCount)
    }

    fun shiftUp(rowCount: Int): ColRowShifter {
        return this.shiftDown(-rowCount)
    }

    fun shiftDown(rowCount: Int): ColRowShifter {
        val i = MathUtils.addIntOrDefault(firstVisibleRow, rowCount)
        val w = MathUtils.addIntOrDefault(lastVisibleRow, rowCount)
        return this.copy(
            visibleRowRange = IntRange(i, w)
        )
    }
}
