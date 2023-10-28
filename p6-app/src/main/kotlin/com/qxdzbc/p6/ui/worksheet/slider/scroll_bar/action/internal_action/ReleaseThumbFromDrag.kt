package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.action.make_scroll_bar_reflect_slider.MakeScrollBarReflectSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionData
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.*
import javax.inject.Inject
import kotlin.math.min

/**
 * TODO need test
 */
@WsScope
class ReleaseThumbFromDrag @Inject constructor(
    private val sliderMs: Ms<GridSlider>,
    val reflect: MakeScrollBarReflectSlider,
)  {

    private var sliderQ by sliderMs

    fun run(data: ScrollBarActionData.ReleaseFromDrag) {

        resizeThumb(data.state, sliderQ)

        recomputeScrollbarLimit(data.state)

        repositionThumbWhenReachEnd(data.state, sliderQ)
    }

    /**
     * Recompute scroll bar limit of [sliderQ] in case scroll bar thumbs reach the start or the end of its rail.
     */
    private fun recomputeScrollbarLimit(scrollBarState: ScrollBarState){
        if(scrollBarState.thumbReachRailStart) {
            sliderQ = sliderQ.resetScrollBarLimit()
        }
        if(scrollBarState.thumbReachRailEnd){
            sliderQ = sliderQ.expandScrollBarLimitsIfNeed()
        }
    }

    /**
     * Resize the thumb so that it reflects the current state of [slider].
     */
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


    /**
     * When a thumb reach the end of its rail, it has to be pulled back, so that users can drag further down. This function computes how much to pull the thumb back.
     */
    private fun repositionThumbWhenReachEnd(scrollBarState: ScrollBarState, slider: GridSlider) {
        if (scrollBarState.thumbReachRailEnd) {
            reflect.reflectThumbPosition(scrollBarState, slider)
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