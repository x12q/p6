package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.di.comp.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultColRangeQualifier
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultRowRangeQualifier
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * A slider that can only shift within bounds defined by [colLimit] and [rowLimit]
 */
@ContributesBinding(WsAnvilScope::class, boundType = GridSlider::class)
data class GridSliderImp(
    private val slider: InternalGridSlider,
    override val colLimit: IntRange,
    override val rowLimit: IntRange,
    override val marginRow: Int?,
    override val marginCol: Int?,
    override val edgeSliderRow: Int,
    override val edgeSliderCol: Int,
) : BaseSlider(),GridSlider {

    @Inject
    constructor(
        slider: InternalGridSlider,
        @DefaultColRangeQualifier
        colLimit: IntRange,
        @DefaultRowRangeQualifier
        rowLimit: IntRange,
    ):this(
        slider = slider,
        colLimit = colLimit,
        rowLimit = rowLimit,
        marginRow = null,
        marginCol = null,
        edgeSliderCol = GridSliderConstants.startingEdgeSliderCol,
        edgeSliderRow = GridSliderConstants.startingEdgeSliderRow,
    )


    init{
        val sliderColOutOfBound = !(slider.firstVisibleCol in colLimit && slider.lastVisibleCol in colLimit)
        val sliderRowOutOfBound = !(slider.firstVisibleRow in rowLimit && slider.lastVisibleRow in rowLimit)
        if(sliderColOutOfBound || sliderRowOutOfBound){
            throw IllegalArgumentException("slider ${slider} doesn't fit in range (c:${colLimit}, r:${rowLimit})")
        }
    }

    override val edgeSliderRowRange: IntRange get() = IntRange(1,edgeSliderRow)

    override val edgeSliderColRange: IntRange get() = IntRange(1,edgeSliderCol)

    override val firstVisibleCol: Int get() = slider.firstVisibleCol

    override val lastVisibleCol: Int get() = slider.lastVisibleCol

    override val visibleColRangeIncludeMargin: IntRange get() = slider.visibleColRange

    override fun setVisibleColRange(i: IntRange): GridSlider {
        if(i==this.visibleColRangeIncludeMargin){
            return this
        }else{
            return this.copy(slider = this.slider.setVisibleColRange(i))
        }
    }

    override val firstVisibleRow: Int get() = slider.firstVisibleRow

    override val lastVisibleRow: Int get() = slider.lastVisibleRow

    override val visibleRowRangeIncludeMargin: IntRange get() = slider.visibleRowRange


    override fun setMarginRow(i: Int?): GridSliderImp {
        return this.copy(
            marginRow = i
        )
    }


    override fun setMarginCol(i: Int?): GridSliderImp {
        return this.copy(
            marginCol = i
        )
    }

    override fun setVisibleRowRange(i: IntRange): GridSliderImp {
        if(i == this.visibleRowRangeIncludeMargin){
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
        fun forPreview(
            visibleColRange: IntRange = WorksheetConstants.defaultVisibleColRange,
            visibleRowRange: IntRange = WorksheetConstants.defaultVisibleRowRange,
            colLimit: IntRange = WorksheetConstants.defaultColRange,
            rowLimit: IntRange = WorksheetConstants.defaultRowRange,
        ): GridSliderImp {
            return GridSliderImp(
                slider = InternalGridSlider(
                    visibleColRange = visibleColRange,
                    visibleRowRange = visibleRowRange,
                ),
                colLimit = colLimit,
                rowLimit = rowLimit
            )
        }
    }
}
