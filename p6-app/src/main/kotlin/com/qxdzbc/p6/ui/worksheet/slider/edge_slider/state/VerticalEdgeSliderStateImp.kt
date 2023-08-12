package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.LimitedSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.EdgeSliderUtils
import javax.inject.Inject
import kotlin.math.max

@WsScope
data class VerticalEdgeSliderStateImp(
    val thumbPositionMs: Ms<DpOffset>,
    val thumbLengthRatioMs: Ms<Float>,
    val maxLengthRatio: Float,
    val minLengthRatio: Float,
    val reductionRatio: Float,
    val sliderStateSt: St<LimitedSlider>,
    val step:Int,
) : VerticalEdgeSliderState {

    @Inject
    constructor(
        sliderStateSt: St<LimitedSlider>,
    ) : this(
        thumbPositionMs = ms(DpOffset.Zero),
        thumbLengthRatioMs = ms(EdgeSliderUtils.maxLength),
        maxLengthRatio = EdgeSliderUtils.maxLength,
        minLengthRatio = EdgeSliderUtils.minLength,
        reductionRatio = EdgeSliderUtils.reductionRate,
        sliderStateSt = sliderStateSt,
        step=30,
    )

    private var lengthRatio by thumbLengthRatioMs

    private val sliderState by sliderStateSt

    override fun thumbLength(railLength: Dp): Dp {
        return railLength * thumbLengthRatioMs.value
    }

    override fun setThumbLengthRatio(ratio: Float) {
        lengthRatio = ratio
    }

    override var thumbPosition: DpOffset by thumbPositionMs

    /**
     * Impose a cap on thumb offset so that it cannot get below 0.
     */
    override fun setThumbOffsetWhenDrag(density: Density, dragDelta: Float) {
        val sliderThumbYPx = with(density) {
            max(thumbPosition.y.toPx() + dragDelta, 0f)
        }

        val sliderOffset = run {
            val slideThumYOffsetDp = with(density) {
                sliderThumbYPx.toDp()
            }
            DpOffset(0.dp, slideThumYOffsetDp)
        }
        thumbPosition = sliderOffset
    }

    override fun recomputeStateWhenThumbReachRailBottom(railLength: Dp) {
        // recompute the thumb length
        lengthRatio = max(lengthRatio * reductionRatio, minLengthRatio)

        val lastRow = sliderState.lastVisibleRow
        sliderState
        val newSize = lastRow + step
        val newPos = lastRow.toFloat()/newSize
        thumbPosition =  DpOffset(0.dp,railLength*newPos)

        // recompute the thumb position
        // thumb position should take into account the real position of the slider.
        /**
         * So to bottom->
         * move the slider up.
         *
         */
        /*
        |
        |
        |
        |
        |__
        |
        |
        |__


        plus 3 hypothetical row
        -> ratio = 5/(5+3)



         */
    }

    /**
     * When reach the top, reset the thumb length to the max value
     */
    override fun recomputeStateWhenThumbReachRailTop() {
        lengthRatio = maxLengthRatio
    }


}