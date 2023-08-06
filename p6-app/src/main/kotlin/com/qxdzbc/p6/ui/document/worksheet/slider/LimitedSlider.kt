package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.DefaultBaseGridSlider
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.DefaultColRangeQualifier
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.DefaultRowRangeQualifier
import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsAnvilScope
import com.qxdzbc.p6.ui.document.worksheet.slider.di.LimitedSliderQualifier
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * A decorator that prevent a slider from shifting over bounds defined by [colLimit] and [rowLimit]
 */
@ContributesBinding(WsAnvilScope::class, boundType = GridSlider::class)
@LimitedSliderQualifier
data class LimitedSlider @Inject constructor(
    @DefaultBaseGridSlider
    private val slider: GridSlider,
    @DefaultColRangeQualifier
    val colLimit: IntRange,
    @DefaultRowRangeQualifier
    val rowLimit: IntRange,
) : BaseSlider() {

    init{
        val sliderColOutOfBound = !(slider.firstVisibleCol in colLimit && slider.lastVisibleCol in colLimit)
        val sliderRowOutOfBound = !(slider.firstVisibleRow in rowLimit && slider.lastVisibleRow in rowLimit)
        if(sliderColOutOfBound || sliderRowOutOfBound){
            throw IllegalArgumentException("slider ${slider.toString()} doesn't fit in range (c:${colLimit}, r:${rowLimit})")
        }
    }

    override val topLeftCell: CellAddress
        get() = slider.topLeftCell

//    override fun setTopLeftCell(cell: CellAddress): GridSlider {
//        if(cell == this.topLeftCell){
//            return this
//        }else{
//            return this.copy(slider = slider.setTopLeftCell(cell))
//        }
//    }

    override val firstVisibleCol: Int get() = slider.firstVisibleCol
    override val lastVisibleCol: Int get() = slider.lastVisibleCol
    override val visibleColRange: IntRange get() = slider.visibleColRange
    override fun setVisibleColRange(i: IntRange): GridSlider {
        if(i==this.visibleColRange){
            return this
        }else{
            return this.copy(slider = this.slider.setVisibleColRange(i))
        }
    }

    override val firstVisibleRow: Int get() = slider.firstVisibleRow
    override val lastVisibleRow: Int get() = slider.lastVisibleRow
    override val visibleRowRange: IntRange get() = slider.visibleRowRange
    override val marginRow: Int? get() = slider.marginRow

    override fun setMarginRow(i: Int?): GridSlider {
        return this.copy(
            slider = slider.setMarginRow(i)
        )
    }

    override val marginCol: Int? get() = slider.marginCol

    override fun setMarginCol(i: Int?): GridSlider {
        return this.copy(
            slider = slider.setMarginCol(i)
        )
    }

    override fun setVisibleRowRange(i: IntRange): GridSlider {
        if(i == this.visibleRowRange){
            return this
        }else{
            return this.copy(slider = this.slider.setVisibleRowRange(i))
        }
    }

    override fun shiftLeft(v: Int): GridSlider {
        if(v<0){
            return this.shiftRight(-v)
        }
        val md = minOf(firstVisibleCol - colLimit.first, v)
        return this.copy(slider = slider.shiftLeft(md))
    }

    override fun shiftRight(v: Int): GridSlider {
        if(v<0){
            return this.shiftLeft(-v)
        }
        val md = minOf(colLimit.last - lastVisibleCol, v)
        return this.copy(slider = slider.shiftRight(md))
    }

    override fun shiftUp(v: Int): GridSlider {
        if(v<0){
            return this.shiftDown(-v)
        }
        val md = minOf(firstVisibleRow - rowLimit.first, v)
        return this.copy(slider = slider.shiftUp(md))
    }

    override fun shiftDown(v: Int): GridSlider {
        if(v<0){
            return this.shiftUp(-v)
        }
        val md = minOf(rowLimit.last - lastVisibleRow, v)
        return this.copy(slider = slider.shiftDown(md))
    }
}
