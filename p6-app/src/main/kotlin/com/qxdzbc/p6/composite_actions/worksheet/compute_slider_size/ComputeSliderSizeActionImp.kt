package com.qxdzbc.p6.composite_actions.worksheet.compute_slider_size

import androidx.compose.ui.unit.*
import com.qxdzbc.common.compose.SizeUtils.toDpSize
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ContributesBinding(P6AnvilScope::class)
class ComputeSliderSizeActionImp @Inject constructor(
    val stateCont: StateContainer,
) : ComputeSliderSizeAction {

    override fun computeSliderProperties(
        availableSpace: IntSize,
        wsState: WorksheetState,
        density: Density
    ) {
        val newSlider = computeSliderProperties(
            oldSlider = wsState.slider,
            sizeConstraint = availableSpace.toDpSize(density),
            anchorCell = wsState.slider.topLeftCell,
            getColWidth = wsState::getColumnWidthOrDefault,
            getRowHeight = wsState::getRowHeightOrDefault,
        )

        wsState.sliderMs.value = newSlider
    }

    data class ComputeResult(
        val index: Int,
        val size: Dp
    )

    /**
     * Compute grid slide size by looping through displayed cells,
     */
    fun computeSliderProperties(
        oldSlider: GridSlider,
        sizeConstraint: DpSize,
        /** a top-left cell to start the computation from **/
        anchorCell: CellAddress,
        getColWidth: (colIndex: Int) -> Dp,
        getRowHeight: (rowIndex: Int) -> Dp,
    ): GridSlider {
        val limitWidth = sizeConstraint.width
        val limitHeight = sizeConstraint.height

        /** Compute slider width **/
        val fromCol = anchorCell.colIndex
        val (toCol, accumWidth) = computeSize(
            fromCol, limitWidth, getColWidth
        )

        /** Compute slider height **/
        val fromRow = anchorCell.rowIndex
        val (toRow, accumHeight) = computeSize(
            fromRow, limitHeight, getRowHeight
        )
//        val lastRow = maxOf(toRow - 1, fromRow)
//        val lastCol = maxOf(toCol - 1, fromCol)

        val lastRow = toRow
        val lastCol = toCol

        val newSlider = oldSlider
            .setVisibleRowRange(fromRow..lastRow)
            .setVisibleColRange(fromCol..lastCol)
            .setMarginRow(computeMarginItem(lastRow, accumHeight, limitHeight))
            .setMarginCol(computeMarginItem(lastCol, accumWidth, limitWidth))

        return newSlider
    }

    fun computeMarginItem(
        lastItemIndex: Int,
        currentSize: Dp,
        limitSize: Dp,
    ): Int? {
        if (currentSize < limitSize) {
            return lastItemIndex + 1
        } else {
            return null
        }
    }

    /**
     * Compute a [ComputeResult] consist of a size and a last-item index.
     */
    fun computeSize(
        itemInitIndex: Int,
        limitSize: Dp,
        getItemSize: (colIndex: Int) -> Dp,
    ): ComputeResult {
        var toIndex = itemInitIndex
        var accumSize = 0.dp

        var lastWidth = accumSize
        while (accumSize < limitSize) {
            val w = getItemSize(toIndex)
            accumSize += w
            lastWidth = w
            toIndex += 1
        }

        /**
         * discard the last item if accum size is larger than size limit
         */
        if (accumSize > limitSize) {
            accumSize -= lastWidth
            toIndex -= 1
        }

        return ComputeResult(
            toIndex,
            accumSize
        )
    }


    /**
     * anchorCell is now the cell that the cursor land in.
     * I would need to detect the movement direction, because:
     * - when the cursor is moving down, the calculation is bottom-> top
     * - when the cursor is moving up, the calculation is top-> bottom
     * - when the cursor is moving to the right, the calculation is from right -> left
     * - when the cursor is moving to the left, the calculation is from left -> right.
     * Take the cursor's position into consideration. When the cursor is at the corner of the slider, probably need to do the computation from there.
     * One of the requirement of this function is that it must ensure the cell at the cursor is shown fully everytime.
     *
     * The margin cell can only be at the bottom and the right side.
     * The ones at the top, and the left cannot contain margin cells.
     *
     * I need to re-read the margin item logic. Can margin item
     *
     */
    fun computeSliderSizeQQQ(
        oldGridSlider: GridSlider,
        sizeConstraint: DpSize,
        /** a top-left cell to start the computation from **/
        anchorCell: CellAddress,
        getColWidth: (colIndex: Int) -> Dp,
        getRowHeight: (rowIndex: Int) -> Dp,
    ): GridSlider {
        val limitWidth = sizeConstraint.width
        val limitHeight = sizeConstraint.height

        val rowResult = computeRow(
            oldGridSlider = oldGridSlider,
            anchorCell = anchorCell,
            limitHeight = limitHeight,
            getRowHeight = getRowHeight
        )

        val colResult = computeCol(
            oldGridSlider = oldGridSlider,
            anchorCell = anchorCell,
            limitWidth = limitWidth,
            getColWidth = getColWidth
        )


        val newSlider = oldGridSlider.let { slider ->
            var s: GridSlider? = slider
            val rowRange = rowResult.itemRangeIncludingMargin

            /**
             * set visible col and row
             */
            s = rowRange?.let {
                s?.setVisibleRowRange(it)
            }
            val colRange = colResult.itemRangeIncludingMargin
            s = colRange?.let {
                s?.setVisibleColRange(it)
            }
            /**
             * set visible margin
             */
            s = s?.setMarginRow(rowResult.margin)
                ?.setMarginCol(colResult.margin)

            /**
             * final slider
             */
            s ?: slider
        }

        return newSlider
    }


    fun computeCol(
        oldGridSlider: GridSlider,
        anchorCell: CellAddress,
        limitWidth: Dp,
        getColWidth: (colIndex: Int) -> Dp,
    ): TwoSideResult {
        val topLeftCell = oldGridSlider.topLeftCell
        val rt = computeTwoWay(
            initIndex = anchorCell.rowIndex,
            limitSize = limitWidth,
            getItemSize = getColWidth,
            checkIndexValidity = { colIndex ->
                colIndex in oldGridSlider.colLimit && colIndex >= topLeftCell.colIndex
            }
        )
        return rt
    }


    fun computeRow(
        oldGridSlider: GridSlider,
        anchorCell: CellAddress,
        limitHeight: Dp,
        getRowHeight: (rowIndex: Int) -> Dp,
    ): TwoSideResult {
        val topLeftCell = oldGridSlider.topLeftCell
        val rt = computeTwoWay(
            initIndex = anchorCell.rowIndex,
            limitSize = limitHeight,
            getItemSize = getRowHeight,
            checkIndexValidity = { rowIndex ->
                rowIndex in oldGridSlider.rowLimit && rowIndex >= topLeftCell.rowIndex
            }
        )
        return rt
    }


    fun computeTwoWay(
        initIndex: Int,
        limitSize: Dp,
        getItemSize: (rowIndex: Int) -> Dp,
        checkIndexValidity: (Int) -> Boolean,
    ): TwoSideResult {
        val upRs = computeUp(
            initIndex = initIndex,
            limitSize = limitSize,
            getItemSize = getItemSize,
            checkIndexValidity = checkIndexValidity
        )


        val downRs = upRs.toIndex?.let { toIndex ->
            computeDown(
                initIndex = toIndex + 1,
                limitSize = limitSize,
                initSize = upRs.accumSize,
                checkIndexValidity = checkIndexValidity,
                getItemSize = getItemSize
            )
        }

        return TwoSideResult(
            fromIndex = upRs.fromIndex,
            toIndex = downRs?.toIndex ?: upRs.toIndex,
            margin = upRs.margin ?: downRs?.margin
        )
    }


    fun computeUp(
        initIndex: Int,
        limitSize: Dp,
        getItemSize: (rowIndex: Int) -> Dp,
        checkIndexValidity: (Int) -> Boolean,
    ): UpResult {

        var toIndex = initIndex
        var accumSize = getItemSize(initIndex)

        if (accumSize > limitSize) {
            return UpResult(
                null, null, toIndex, accumSize
            )
        } else {
            while (accumSize < limitSize) {
                val nextIndex = toIndex - 1
                if (checkIndexValidity(nextIndex)) {
                    val itemSize = getItemSize(nextIndex)
                    val nextSize = accumSize + itemSize
                    if (nextSize < limitSize) {
                        accumSize = nextSize
                        toIndex = nextIndex
                    } else {
                        break
                    }
                } else {
                    break
                }
            }

            return UpResult(
                toIndex, initIndex, null, accumSize
            )
        }
    }


    /**
     * Compute downward, [initIndex] will be included into the computation
     */
    fun computeDown(
        initIndex: Int,
        limitSize: Dp,
        initSize: Dp,
        checkIndexValidity: (Int) -> Boolean,
        getItemSize: (colIndex: Int) -> Dp,
    ): DownResult {
        var toIndex = initIndex
        var accumSize = initSize + getItemSize(initIndex)

        if (accumSize > limitSize) {
            return DownResult(null, null, toIndex)
        } else if (accumSize == limitSize) {
            return DownResult(toIndex, toIndex,null)
        }else {
            var moreThanSizeLimit = false
            while (accumSize < limitSize) {
                val nextIndex = toIndex + 1
                if (checkIndexValidity(nextIndex)) {
                    val itemSize = getItemSize(nextIndex)
                    val nextSize = accumSize + itemSize
                    if (nextSize <= limitSize) {
                        accumSize = nextSize
                        toIndex = nextIndex
                    } else {
                        if(nextSize > limitSize){
                            moreThanSizeLimit = true
                            break
                        }
                    }
                } else {
                    break
                }
            }

            val margin = if (moreThanSizeLimit) {
                toIndex + 1
            } else {
                null
            }

            return DownResult(
                fromIndex = initIndex,
                toIndex = toIndex,
                margin = margin,
            )
        }
    }


    data class UpResult(
        val fromIndex: Int?,
        val toIndex: Int?,
        val margin: Int?,
        val accumSize: Dp,
    )

    data class DownResult(
        val fromIndex: Int?,
        val toIndex: Int?,
        val margin: Int?,
    )

    data class TwoSideResult(
        val fromIndex: Int?,
        val toIndex: Int?,
        val margin: Int?,
    ) {
        val itemRangeIncludingMargin: IntRange?
            get() {
                val f = fromIndex
                val t = margin ?: toIndex
                if(f!=null && t!=null) {
                    return f .. t
                }else{
                    return null
                }
            }
    }

}
