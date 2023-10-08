package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ReleaseFromDragData
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionData
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalScrollBar
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarType
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlin.math.min

@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class InternalScrollBarActionImp @Inject constructor(
    private val wsGetter: WorksheetStateGetter,
    private val sliderMs: Ms<GridSlider>,
    @ForVerticalScrollBar
    private val thumbPositionConverterForVerticalScrollBar: ThumbPositionConverter,
    @ForVerticalScrollBar
    private val thumbPositionConverterForHorizontalScrollBar: ThumbPositionConverter,
) : InternalScrollBarAction {

    private var slider by sliderMs

    /**
     * When a scroll bar is drag, shift the worksheet slider either by some number of columns or rows. That number is computed from the position of the thumb.
     */
    override fun drag(data: ScrollBarActionData.Drag) {
        if (!data.data.thumbReachRailEnd) {
            when (data.data.scrollBarType) {
                ScrollBarType.Vertical -> {
                    val edgeRowRange = slider.scrollBarRowRange
                    val newEdgeRow =
                        thumbPositionConverterForVerticalScrollBar.convertThumbPositionToIndex(edgeRowRange)

                    // move the top row to this
                    val shiftCount = newEdgeRow - slider.topLeftCell.rowIndex

                    if (shiftCount != 0) {
                        val newSlider = slider.shiftDown(shiftCount)
                        wsGetter.get()?.updateSliderAndRefreshDependentStates(newSlider)
                        slider = newSlider
                    }
                }

                ScrollBarType.Horizontal -> {
                    val edgeColRange = slider.scrollBarColRange
                    val newEdgeCol =
                        thumbPositionConverterForHorizontalScrollBar.convertThumbPositionToIndex(edgeColRange)

                    // move the left col to this
                    val shiftCount = newEdgeCol - slider.topLeftCell.colIndex

                    if (shiftCount != 0) {
                        val newSlider = slider.shiftRight(shiftCount)
                        wsGetter.get()?.updateSliderAndRefreshDependentStates(newSlider)
                        slider = newSlider
                    }
                }
            }
        }
    }

    override fun releaseFromDrag(data: ReleaseFromDragData) {
        resizeThumb(data.state, slider)

        recomputeScrollbarLimit(data.state)

        repositionThumbWhenReachEnd(data.state, slider)
    }

    private fun recomputeScrollbarLimit(scrollBarState: ScrollBarState){
        if(scrollBarState.thumbReachRailStart) {
            slider = slider.resetScrollBarLimit()
        }
        if(scrollBarState.thumbReachRailEnd){
            slider = slider.expandScrollBarLimitIfNeed()
        }
    }

    private fun resizeThumb(scrollBarState: ScrollBarState, slider: GridSlider) {
        val sbt = scrollBarState
        if (sbt.thumbReachRailStart) {
            scrollBarState.resetThumbLength()
        } else {
            if (sbt.thumbReachRailEnd) {
                val positionRatio = when (sbt.type) {
                    ScrollBarType.Vertical -> {
                        val numberOfDisplayRow = slider.visibleRowRangeIncludeMargin.count()
                        val numberOfScrollBarRow = slider.scrollBarRowRange.count()
                        shrinkThumbLengthRatioFormula(
                            numberOfVisibleItem = numberOfDisplayRow,
                            numberOfScrollBarItem = numberOfScrollBarRow,
                            currentLengthRatio = sbt.thumbLengthRatio
                        )
                    }

                    ScrollBarType.Horizontal -> {
                        val numberOfDisplayCol = slider.visibleColRangeIncludeMargin.count()
                        val numberOfScrollBarCol = slider.scrollBarColRange.count()
                        shrinkThumbLengthRatioFormula(
                            numberOfVisibleItem = numberOfDisplayCol,
                            numberOfScrollBarItem = numberOfScrollBarCol,
                            currentLengthRatio = sbt.thumbLengthRatio
                        )
                    }
                }
                scrollBarState.setThumbLengthRatio(positionRatio)
            }
        }
    }


    private fun repositionThumbWhenReachEnd(scrollBarState: ScrollBarState, slider: GridSlider) {
        val sbt = scrollBarState
        if (sbt.thumbReachRailEnd) {
            val r = when (sbt.type) {
                ScrollBarType.Vertical -> {
                    val numberOfDisplayRow = slider.visibleRowRangeIncludeMargin.last.toFloat()
                    val numberOfScrollBarRow = slider.scrollBarRowRange.last
                    numberOfDisplayRow / numberOfScrollBarRow
                }

                ScrollBarType.Horizontal -> {
                    val numberOfDisplayCol = slider.visibleColRangeIncludeMargin.last.toFloat()
                    val numberOfScrollBarCol = slider.scrollBarColRange.last
                    numberOfDisplayCol / numberOfScrollBarCol
                }
            }
            sbt.setThumbPositionRatioViaEffectivePositionRatio(r)
        }

    }

    /**
     * A formula to shrink thumb length with certain constraint and easing.
     *
     * [easingFactor] is here so that the thumb does not shrink too fast.
     *
     * [reductionRate] is how much the thumb length should be reduced based on the current length.
     */
    private fun shrinkThumbLengthRatioFormula(
        numberOfVisibleItem: Int,
        numberOfScrollBarItem: Int,
        currentLengthRatio: Float,
        easingFactor: Int = 15,
        reductionRate: Float = 0.1f,
    ): Float {
        val effectivePosRatio = numberOfVisibleItem.toFloat() * easingFactor / numberOfScrollBarItem
        return min(effectivePosRatio, currentLengthRatio * (1f - reductionRate))
    }

}