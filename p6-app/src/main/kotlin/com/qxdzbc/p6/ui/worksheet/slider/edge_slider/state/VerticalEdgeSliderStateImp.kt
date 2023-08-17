package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
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
    val moveBackRatio:Float,
    val thumbLayoutCoorMs: Ms<P6LayoutCoor?>,
    val railLayoutCoorMs: Ms<P6LayoutCoor?>,
) : VerticalEdgeSliderState {

    @Inject
    constructor() : this(
        thumbPositionMs = ms(DpOffset.Zero),
        thumbLengthRatioMs = ms(EdgeSliderUtils.maxLength),
        maxLengthRatio = EdgeSliderUtils.maxLength,
        minLengthRatio = EdgeSliderUtils.minLength,
        reductionRatio = EdgeSliderUtils.reductionRate,
        moveBackRatio = 0.7f,
        thumbLayoutCoorMs = ms(null),
        railLayoutCoorMs = ms(null),
    )

    private var _thumbLengthRatio by thumbLengthRatioMs

    override val railLengthPx: Float?
        by derivedStateOf { railLayoutCoor?.boundInWindow?.height }

    override var thumbLayoutCoor: P6LayoutCoor? by thumbLayoutCoorMs

    override var railLayoutCoor: P6LayoutCoor? by railLayoutCoorMs

    override fun computeRelativeThumbLength(railLength: Dp): Dp {
        return railLength * thumbLengthRatioMs.value
    }

    override fun setThumbLengthRatio(ratio: Float) {
        _thumbLengthRatio = ratio
    }

    override var thumbPosition: DpOffset by thumbPositionMs

    /**
     * When thumb is dragged by [dragDelta] px, change the offset of the thumb so that it follow the pointer.
     * Impose a cap on thumb offset so that it cannot get below 0.
     */
    override fun setThumbOffsetWhenDrag(
        density: Density, dragDelta: Float, railLength: Dp,
    ) {
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

    /**
     * Shorten thumb length and move it back up when thumb reaches rail bottom
     */
    override fun recomputeStateWhenThumbReachRailBottom(railLength: Dp) {
        // recompute the thumb length
        _thumbLengthRatio = max(_thumbLengthRatio * reductionRatio, minLengthRatio)

//        val lastRow = sliderState.lastVisibleRow

//        val newSize = lastRow + step
//        val newPos = lastRow.toFloat() / newSize

        thumbPosition = DpOffset(0.dp, railLength * moveBackRatio)

    }

    /**
     * When reach the top, reset the thumb length to the max value
     */
    override fun recomputeStateWhenThumbReachRailTop() {
        _thumbLengthRatio = maxLengthRatio
    }

    override fun recomputeStateWhenThumbIsDragged(density: Density, delta: Float) {

        val railLength: Dp = with(density) { railLengthPx?.toDp() } ?: 0.dp
        setThumbOffsetWhenDrag(density, delta, railLength)

        if (thumbReachRailBottom) {
            recomputeStateWhenThumbReachRailBottom(railLength)
        } else if (thumbReachRailTop) {
            recomputeStateWhenThumbReachRailTop()
        }
    }

    override val slideRatio:Float? by derivedStateOf {
        val ratio = railLengthPx?.let { rl ->
            railLayoutCoor?.boundInWindow?.bottom?.let{railBotY->
                thumbLayoutCoor?.boundInWindow?.bottom?.let { thumbBotY ->
                    val thumbLength = rl * _thumbLengthRatio
                    if(railBotY!=0f){
                        (thumbBotY-thumbLength)/(railBotY-thumbLength)
                    }else{
                        null
                    }
                }
            }
        }

        val rt = ratio?.coerceIn(0f,1f)

        rt
    }


    override val thumbReachRailBottom: Boolean
        get() {
            val thumbYBottom = thumbLayoutCoor?.boundInWindow?.bottom
            val railYBottom = railLayoutCoor?.boundInWindow?.bottom
            val rt = thumbYBottom != null && railYBottom != null && thumbYBottom == railYBottom
            return rt
        }

    override val thumbReachRailTop: Boolean
        get() {
            val thumbYTop = thumbLayoutCoor?.boundInWindow?.top
            val railYTop = railLayoutCoor?.boundInWindow?.top
            val rt = railYTop != null && thumbYTop != null && railYTop == thumbYTop
            return rt
        }
}