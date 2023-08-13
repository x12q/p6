package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.ui.worksheet.di.comp.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.slider.di.qualifiers.UnlimitedGridSlider
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultColRangeQualifier
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultRowRangeQualifier
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * A slider that can only shift within bounds defined by [colLimit] and [rowLimit]
 */
@ContributesBinding(WsAnvilScope::class, boundType = GridSlider::class)
data class GridSliderImp constructor(
    private val slider: GridSlider,
    override val colLimit: IntRange,
    override val rowLimit: IntRange,
    override val phantomRowMargin: Int,
) : BaseSlider(),GridSlider {

    @Inject constructor(
        @UnlimitedGridSlider
        slider: GridSlider,
        @DefaultColRangeQualifier
        colLimit: IntRange,
        @DefaultRowRangeQualifier
        rowLimit: IntRange,
    ):this(
        slider = slider,
        colLimit = colLimit,
        rowLimit = rowLimit,
        phantomRowMargin = 30,
    )


    init{
        val sliderColOutOfBound = !(slider.firstVisibleCol in colLimit && slider.lastVisibleCol in colLimit)
        val sliderRowOutOfBound = !(slider.firstVisibleRow in rowLimit && slider.lastVisibleRow in rowLimit)
        if(sliderColOutOfBound || sliderRowOutOfBound){
            throw IllegalArgumentException("slider ${slider} doesn't fit in range (c:${colLimit}, r:${rowLimit})")
        }
    }

    override val topLeftCell: CellAddress
        get() = slider.topLeftCell

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

    override fun setMarginRow(i: Int?): GridSliderImp {
        return this.copy(
            slider = slider.setMarginRow(i)
        )
    }

    override val marginCol: Int? get() = slider.marginCol

    override fun setMarginCol(i: Int?): GridSliderImp {
        return this.copy(
            slider = slider.setMarginCol(i)
        )
    }

    override fun setVisibleRowRange(i: IntRange): GridSliderImp {
        if(i == this.visibleRowRange){
            return this
        }else{
            return this.copy(slider = this.slider.setVisibleRowRange(i))
        }
    }

    override fun shiftLeft(colCount: Int): GridSliderImp {
        if(colCount<0){
            return this.shiftRight(-colCount)
        }
        val md = minOf(firstVisibleCol - colLimit.first, colCount)
        return this.copy(slider = slider.shiftLeft(md))
    }

    override fun shiftRight(colCount: Int): GridSliderImp {
        if(colCount<0){
            return this.shiftLeft(-colCount)
        }
        val md = minOf(colLimit.last - lastVisibleCol, colCount)
        return this.copy(slider = slider.shiftRight(md))
    }

    override fun shiftUp(rowCount: Int): GridSliderImp {
        if(rowCount<0){
            return this.shiftDown(-rowCount)
        }
        val md = minOf(firstVisibleRow - rowLimit.first, rowCount)
        return this.copy(slider = slider.shiftUp(md))
    }

    override fun shiftDown(rowCount: Int): GridSliderImp {
        if(rowCount<0){
            return this.shiftUp(-rowCount)
        }
        val remainingRow = rowLimit.last - lastVisibleRow
        val md = minOf(remainingRow, rowCount)
        return this.copy(slider = slider.shiftDown(md))
    }

    companion object{
        fun forPreview(): GridSliderImp {
            return GridSliders.create()
        }
    }
}
