package com.qxdzbc.p6.ui.worksheet.slider.action.make_scroll_bar_reflect_slider

import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarType
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs
import kotlin.math.min


@Singleton
@ContributesBinding(P6AnvilScope::class)
class MakeScrollBarReflectSliderImp @Inject constructor() : MakeScrollBarReflectSlider {

    override fun reflect(
        scrollBarState: ScrollBarState,
        slider: GridSlider,
        easingFactor: Int,
        isIncreasing:Boolean
    ) {
        reflectThumbPosition(scrollBarState, slider)
        val reductionRate = if(isIncreasing){
            0.008f
        }else{
            -0.008f
        }
        reflectThumbSize(scrollBarState, slider, easingFactor,reductionRate)
    }

    /**
     *  Reflect data from [slider] onto the thumb in [scrollBarState]
     */
    private fun reflectThumbPosition(scrollBarState: ScrollBarState, slider: GridSlider) {
        when (scrollBarState.type) {
            ScrollBarType.Vertical -> {
                if (slider.firstVisibleRow == 1) {
                    scrollBarState.resetThumbPosition()
                } else {
                    val r = slider.computeScrolledRowPercentage()
                    scrollBarState.setThumbPositionRatioViaEffectivePositionRatio(r)
                }
            }

            ScrollBarType.Horizontal -> {
                if (slider.firstVisibleCol == 1) {
                    scrollBarState.resetThumbPosition()
                } else {
                    val r = slider.computeScrolledColPercentage()
                    scrollBarState.setThumbPositionRatioViaEffectivePositionRatio(r)
                }
            }
        }
    }
    /**
     * make thumb size of [scrollBarState] reflect the state of grid slider.
     * The more col/row is scrolled, the smaller the thumb. And vise versa.
     *
     * [easingFactor] is to prevent the thumb from shrinking too fast. The bigger the [easingFactor], the slower it shrinks. Must be non-zero.
     *
     * [reductionRate] is how much the thumb length should be reduced based on the current length. Must be between [0,1]
     */
    private fun reflectThumbSize(
        scrollBarState: ScrollBarState,
        slider: GridSlider,
        easingFactor: Int,
        reductionRate: Float,
    ) {

        require(easingFactor != 0)
        require(abs(reductionRate) in 0f..1f)

        when (scrollBarState.type) {
            ScrollBarType.Vertical -> {
                if (slider.firstVisibleRow == 1) {
                    scrollBarState.resetThumbLength()
                } else {
                    val numberOfDisplayRow = slider.visibleRowRangeIncludeMargin.count()
                    val numberOfScrollBarRow = slider.scrollBarRowRange.count()
                    val positionRatio = shrinkThumbLengthRatioFormula(
                        numberOfVisibleItem = numberOfDisplayRow,
                        numberOfScrollBarItem = numberOfScrollBarRow,
                        currentLengthRatio = scrollBarState.thumbLengthRatio,
                        easingFactor = easingFactor,
                        reductionRate = reductionRate
                    )
                    scrollBarState.setThumbLengthRatio(positionRatio)
                }
            }

            ScrollBarType.Horizontal -> {
                if (slider.firstVisibleCol == 1) {
                    scrollBarState.resetThumbLength()
                } else {
                    val numberOfDisplayCol = slider.visibleColRangeIncludeMargin.count()
                    val numberOfScrollBarCol = slider.scrollBarColRange.count()
                    val positionRatio = shrinkThumbLengthRatioFormula(
                        numberOfVisibleItem = numberOfDisplayCol,
                        numberOfScrollBarItem = numberOfScrollBarCol,
                        currentLengthRatio = scrollBarState.thumbLengthRatio,
                        easingFactor = easingFactor,
                        reductionRate = reductionRate
                    )
                    scrollBarState.setThumbLengthRatio(positionRatio)
                }
            }
        }
    }

    /**
     * A formula to compute (shrink) thumb length with certain constraint and easing.
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