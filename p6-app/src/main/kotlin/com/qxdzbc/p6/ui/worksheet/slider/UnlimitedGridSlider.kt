package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.p6.common.utils.MathUtils
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.di.qualifiers.NullInt
import com.qxdzbc.p6.ui.worksheet.slider.di.qualifiers.UnlimitedGridSlider
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultVisibleColRange
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultVisibleRowRange
import com.qxdzbc.p6.ui.worksheet.di.comp.WsAnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * a [GridSlider] that has not limits.For internal use only
 */
@ContributesBinding(WsAnvilScope::class, boundType = GridSlider::class)
@UnlimitedGridSlider
data class UnlimitedGridSlider @Inject constructor(
    @DefaultVisibleColRange
    override val visibleColRange: IntRange,
    @DefaultVisibleRowRange
    override val visibleRowRange: IntRange,
    @NullInt
    override val marginRow: Int? = null,
    @NullInt
    override val marginCol: Int? = null,
) : BaseSlider() {

    override val phantomRowMargin: Int
        get() = throw UnsupportedOperationException("not supported")

    override val colLimit: IntRange
        get() = throw UnsupportedOperationException("not supported")

    override val rowLimit: IntRange
        get() = throw UnsupportedOperationException("not supported")

    override fun setVisibleColRange(i: IntRange): GridSlider {
        if (i == this.visibleColRange) {
            return this
        } else {
            return this.copy(visibleColRange = i)
        }
    }

    override fun setMarginRow(i: Int?): GridSlider = this.copy(marginRow = i)

    override fun setMarginCol(i: Int?): GridSlider = this.copy(marginCol = i)

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
