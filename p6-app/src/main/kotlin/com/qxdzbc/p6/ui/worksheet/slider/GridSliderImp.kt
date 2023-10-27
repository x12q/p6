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
    private val colRowShifter: ColRowShifter,
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
        colRowShifter: ColRowShifter,
        @DefaultColRangeQualifier
        colLimit: IntRange,
        @DefaultRowRangeQualifier
        rowLimit: IntRange,
    ) : this(
        colRowShifter = colRowShifter,
        colLimit = colLimit,
        rowLimit = rowLimit,
        marginRow = null,
        marginCol = null,
        scrollBarLastCol = GridSliderConstants.startingEdgeSliderCol + colRowShifter.lastVisibleCol,
        scrollBarLastRow = GridSliderConstants.startingEdgeSliderRow + colRowShifter.lastVisibleRow,
    )


    /**
     * Conduct check to make sure the initial state is legal
     */
    init {
        val sliderColOutOfBound = !(colRowShifter.firstVisibleCol in colLimit && colRowShifter.lastVisibleCol in colLimit)
        val sliderRowOutOfBound = !(colRowShifter.firstVisibleRow in rowLimit && colRowShifter.lastVisibleRow in rowLimit)
        if (sliderColOutOfBound || sliderRowOutOfBound) {
            throw IllegalArgumentException("slider ${colRowShifter} doesn't fit in range (c:${colLimit}, r:${rowLimit})")
        }
    }

    override val scrollBarRowRange: IntRange get() = IntRange(1, scrollBarLastRow)

    override val scrollBarColRange: IntRange get() = IntRange(1, scrollBarLastCol)

    override val firstVisibleCol: Int get() = colRowShifter.firstVisibleCol

    override val lastVisibleCol: Int get() = colRowShifter.lastVisibleCol

    override val visibleColRangeIncludeMargin: IntRange get() = colRowShifter.visibleColRange

    override fun setVisibleColRange(i: IntRange): GridSlider {
        if (i == this.visibleColRangeIncludeMargin) {
            return this
        } else {
            val rt = this
                .copy(colRowShifter = this.colRowShifter.setVisibleColRange(i))
                .expandScrollBarLimitIfNeed()

            return rt
        }
    }

    override val firstVisibleRow: Int get() = colRowShifter.firstVisibleRow

    override val lastVisibleRow: Int get() = colRowShifter.lastVisibleRow

    override val visibleRowRangeIncludeMargin: IntRange get() = colRowShifter.visibleRowRange

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
                .copy(colRowShifter = this.colRowShifter.setVisibleRowRange(i))
                .expandScrollBarLimitIfNeed()
            return rt
        }
    }

    override fun shiftLeft(colCount: Int): GridSliderImp {
        if (colCount < 0) {
            return this.shiftRight(-colCount)
        }
        val md = minOf(firstVisibleCol - colLimit.first, colCount)
        val rt = this.copy(colRowShifter = colRowShifter.shiftLeft(md))
        return rt
    }

    override fun shiftRight(colCount: Int): GridSliderImp {

        if (colCount < 0) {
            return this.shiftLeft(-colCount)
        }
        val md = minOf(colLimit.last - lastVisibleCol, colCount)
        return this
            .copy(colRowShifter = colRowShifter.shiftRight(md))
    }

    override fun shiftUp(rowCount: Int): GridSliderImp {
        if (rowCount < 0) {
            return this.shiftDown(-rowCount)
        }
        val md = minOf(firstVisibleRow - rowLimit.first, rowCount)
        return this.copy(colRowShifter = colRowShifter.shiftUp(md))
    }

    override fun shiftDown(rowCount: Int): GridSliderImp {
        if (rowCount < 0) {
            return this.shiftUp(-rowCount)
        }
        /**
         * Can only shift down, if there's still row down to shift.
         */
        val remainingRowToLimit = rowLimit.last - lastVisibleRow
        val shiftableRow = minOf(remainingRowToLimit, rowCount)
        val rt = this.copy(colRowShifter = colRowShifter.shiftDown(shiftableRow))
        return rt
    }

    /**
     * expand [scrollBarLastCol] and [scrollBarLastRow] if [colRowShifter] reaches or goes out of those limits.
     */
    override fun expandScrollBarLimitIfNeed(margin: Int): GridSliderImp {

        var rt = this

        val wentOutOfColLimit =
            colRowShifter.lastVisibleCol >= scrollBarLastCol
                || scrollBarLastCol - colRowShifter.lastVisibleCol > margin

        if (wentOutOfColLimit) {
            rt = rt.copy(scrollBarLastCol = colRowShifter.lastVisibleCol + margin)
        }

        val wentOutOfRowLimit =
            colRowShifter.lastVisibleRow >= scrollBarLastRow
                || scrollBarLastRow - colRowShifter.lastVisibleRow > margin

        if (wentOutOfRowLimit) {
            rt = rt.copy(scrollBarLastRow = colRowShifter.lastVisibleRow + margin)
        }

        return rt
    }

    override fun shrinkScrollBarLimitIfNeed(shrinkTo: Int): GridSliderImp {

        val rowDif = (lastVisibleRow - scrollBarLastRow) - shrinkTo

        var rt = this

        if(rowDif>0){
            rt = rt.copy(
                scrollBarLastRow = GridSliderConstants.startingEdgeSliderRow + colRowShifter.lastVisibleRow,
            )
        }

        val colDif = (lastVisibleCol - scrollBarLastCol) - shrinkTo

        if(colDif>0){
            rt = rt.copy(
                scrollBarLastCol = GridSliderConstants.startingEdgeSliderCol + colRowShifter.lastVisibleCol,
            )
        }
        return rt
    }

    override fun resetScrollBarLimit(): GridSlider {
        return this.copy(
            scrollBarLastCol = GridSliderConstants.startingEdgeSliderCol + colRowShifter.lastVisibleCol,
            scrollBarLastRow = GridSliderConstants.startingEdgeSliderRow + colRowShifter.lastVisibleRow,
        )
    }

    /**
     * TODO this computation is not perfect. I can't scroll up to 0 because this computation is always non zero.
     */
    override fun computeScrolledRowPercentage(): Float {
        val visibleRowCount = visibleRowRangeIncludeMargin.count()
        val lastVisibleRow = visibleRowRangeIncludeMargin.last.toFloat() - visibleRowCount
        val scrollRowLimit = scrollBarRowRange.last - visibleRowCount
        return lastVisibleRow / scrollRowLimit
    }

    override fun computeScrolledColPercentage(): Float {
        val visibleColCount = visibleColRangeIncludeMargin.count()
        val lastVisibleCol = visibleColRangeIncludeMargin.last.toFloat() - visibleColCount
        val scrollColLimit = scrollBarColRange.last - visibleColCount
        return lastVisibleCol / scrollColLimit
    }

    companion object {
        fun forPreview(
            visibleColRange: IntRange = WorksheetConstants.defaultVisibleColRange,
            visibleRowRange: IntRange = WorksheetConstants.defaultVisibleRowRange,
            colLimit: IntRange = WorksheetConstants.defaultColRange,
            rowLimit: IntRange = WorksheetConstants.defaultRowRange,
        ): GridSliderImp {
            return GridSliderImp(
                colRowShifter = ColRowShifter(
                    visibleColRange = visibleColRange,
                    visibleRowRange = visibleRowRange,
                ),
                colLimit = colLimit,
                rowLimit = rowLimit
            )
        }
    }
}
