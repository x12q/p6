package com.emeraldblast.p6.ui.document.worksheet.slider

import com.emeraldblast.p6.app.common.utils.MathUtils
import com.emeraldblast.p6.app.common.utils.add
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.di.state.ws.DefaultVisibleColRange
import com.emeraldblast.p6.di.state.ws.DefaultVisibleRowRange
import javax.inject.Inject

/**
 * immutable [GridSlider]
 */
data class GridSliderImp @Inject constructor(
    @DefaultVisibleColRange
    override val visibleColRange: IntRange,
    @DefaultVisibleRowRange
    override val visibleRowRange: IntRange,
) : BaseSlider() {
    override val topLeftCell: CellAddress get() = CellAddress(this.firstVisibleCol, this.firstVisibleRow)
    override val firstVisibleCol: Int get() = visibleColRange.first
    override val lastVisibleCol: Int get() = visibleColRange.last
    override fun setVisibleColRange(i: IntRange): GridSlider {
        if (i == this.visibleColRange) {
            return this
        } else {
            return this.copy(visibleColRange = i)
        }
    }

    override val firstVisibleRow: Int get() = visibleRowRange.first
    override val lastVisibleRow: Int get() = visibleRowRange.last
    override fun setVisibleRowRange(i: IntRange): GridSlider {
        if (i == this.visibleRowRange) {
            return this
        } else {
            return this.copy(visibleRowRange = i)
        }
    }

    override fun shiftLeft(v: Int): GridSlider {
        val i = MathUtils.addIntOrDefault(firstVisibleCol, -v)
        val w = MathUtils.addIntOrDefault(lastVisibleCol, -v)
        return this.copy(
            visibleColRange = IntRange(i, w)
        )
    }

    override fun shiftRight(v: Int): GridSlider {
        return this.shiftLeft(-v)
    }

    override fun shiftUp(v: Int): GridSlider {
        return this.shiftDown(-v)
    }

    override fun shiftDown(v: Int): GridSlider {
        val i = MathUtils.addIntOrDefault(firstVisibleRow, v)
        val w = MathUtils.addIntOrDefault(lastVisibleRow, v)
        return this.copy(
            visibleRowRange = IntRange(i, w)
        )
    }
}
