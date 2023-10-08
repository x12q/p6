package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
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
    /**
     * 1-> 1 million
     */
    override val colLimit: IntRange,
    /**
     * 1-> 1 million
     */
    override val rowLimit: IntRange,
    override val marginRow: Int?,
    override val marginCol: Int?,
    override val scrollBarLastRow: Int,
    override val scrollBarLastCol: Int,
) : BaseSlider(), GridSlider {

    @Inject
    constructor(
        slider: InternalGridSlider,
        @DefaultColRangeQualifier
        colLimit: IntRange,
        @DefaultRowRangeQualifier
        rowLimit: IntRange,
    ) : this(
        slider = slider,
        colLimit = colLimit,
        rowLimit = rowLimit,
        marginRow = null,
        marginCol = null,
        scrollBarLastCol = GridSliderConstants.startingEdgeSliderCol + slider.lastVisibleCol,
        scrollBarLastRow = GridSliderConstants.startingEdgeSliderRow + slider.lastVisibleRow,
    )


    /**
     * Conduct check to make sure the initial state is legal
     */
    init {
        val sliderColOutOfBound = !(slider.firstVisibleCol in colLimit && slider.lastVisibleCol in colLimit)
        val sliderRowOutOfBound = !(slider.firstVisibleRow in rowLimit && slider.lastVisibleRow in rowLimit)
        if (sliderColOutOfBound || sliderRowOutOfBound) {
            throw IllegalArgumentException("slider ${slider} doesn't fit in range (c:${colLimit}, r:${rowLimit})")
        }
    }

    override val scrollBarRowRange: IntRange get() = IntRange(1, scrollBarLastRow)

    override val scrollBarColRange: IntRange get() = IntRange(1, scrollBarLastCol)

    override val firstVisibleCol: Int get() = slider.firstVisibleCol

    override val lastVisibleCol: Int get() = slider.lastVisibleCol

    override val visibleColRangeIncludeMargin: IntRange get() = slider.visibleColRange

    override fun setVisibleColRange(i: IntRange): GridSlider {
        if (i == this.visibleColRangeIncludeMargin) {
            return this
        } else {
            val rt = this
                .copy(slider = this.slider.setVisibleColRange(i))
                .updateEdgeSliderLimit()

            return rt
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
        if (i == this.visibleRowRangeIncludeMargin) {
            return this
        } else {
            val rt = this
                .copy(slider = this.slider.setVisibleRowRange(i))
                .updateEdgeSliderLimit()
            return rt
        }
    }

    override fun shiftLeft(colCount: Int): GridSliderImp {
        if (colCount < 0) {
            return this.shiftRight(-colCount)
        }
        val md = minOf(firstVisibleCol - colLimit.first, colCount)
        val rt = this
            .copy(slider = slider.shiftLeft(md))
            .updateEdgeSliderLimit()
        return rt
    }

    override fun shiftRight(colCount: Int): GridSliderImp {

        if (colCount < 0) {
            return this
                .shiftLeft(-colCount)
                .updateEdgeSliderLimit()
        }
        val md = minOf(colLimit.last - lastVisibleCol, colCount)
        return this
            .copy(slider = slider.shiftRight(md))
            .updateEdgeSliderLimit()
    }

    override fun shiftUp(rowCount: Int): GridSliderImp {
        if (rowCount < 0) {
            return this
                .shiftDown(-rowCount)
                .updateEdgeSliderLimit()
        }
        val md = minOf(firstVisibleRow - rowLimit.first, rowCount)
        return this
            .copy(slider = slider.shiftUp(md))
            .updateEdgeSliderLimit()
    }

    override fun shiftDown(rowCount: Int): GridSliderImp {
        if (rowCount < 0) {
            return this
                .shiftUp(-rowCount)
                .updateEdgeSliderLimit()
        }
        /**
         * Can only shift down, if there's still row down to shift.
         */
        val remainingRowToLimit = rowLimit.last - lastVisibleRow
        val shiftableRow = minOf(remainingRowToLimit, rowCount)
        val rt = this
            .copy(slider = slider.shiftDown(shiftableRow))
            .updateEdgeSliderLimit()
        return rt
    }

    /**
     * expand or shrink [scrollBarLastCol] and [scrollBarLastRow] if [slider] reaches or goes out of those limits.
     */
    fun updateEdgeSliderLimit(margin: Int = GridSliderConstants.edgeAdditionItemCount): GridSliderImp {

        var rt = this

        val wentOutOfColLimit =
            slider.lastVisibleCol >= scrollBarLastCol
                || scrollBarLastCol - slider.lastVisibleCol > margin

        if (wentOutOfColLimit) {
            rt = rt.copy(scrollBarLastCol = slider.lastVisibleCol + margin)
        }

        val wentOutOfRowLimit =
            slider.lastVisibleRow >= scrollBarLastRow
                || scrollBarLastRow - slider.lastVisibleRow > margin

        if (wentOutOfRowLimit) {
            rt = rt.copy(scrollBarLastRow = slider.lastVisibleRow + margin)
        }

        return rt
    }

    companion object {
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
