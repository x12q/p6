package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.qxdzbc.common.FloatUtils
import com.qxdzbc.common.FloatUtils.guardFloat
import com.qxdzbc.common.FloatUtils.guardFloat01
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.EdgeSliderUtils
import javax.inject.Inject
import kotlin.math.max

@WsScope
data class VerticalEdgeSliderStateImp(
    val thumbLengthRatioMs: Ms<Float>,
    val thumbPositionRatioMs: Ms<Float>,
    val maxLengthRatio: Float,
    val minLengthRatio: Float,
    val reductionRatio: Float,
    val moveBackRatio: Float,
    val thumbLayoutCoorMs: Ms<P6LayoutCoor?>,
    val railLayoutCoorMs: Ms<P6LayoutCoor?>,
) : VerticalEdgeSliderState {

    @Inject
    constructor() : this(
        thumbLengthRatioMs = ms(EdgeSliderUtils.maxLength),
        maxLengthRatio = EdgeSliderUtils.maxLength,
        minLengthRatio = EdgeSliderUtils.minLength,
        reductionRatio = EdgeSliderUtils.reductionRate,
        moveBackRatio = 0.7f,
        thumbLayoutCoorMs = ms(null),
        railLayoutCoorMs = ms(null),
        thumbPositionRatioMs = ms(EdgeSliderUtils.startingThumbPositionRatio)
    )

    private var _thumbLengthRatio by thumbLengthRatioMs

    override val railLengthPx: Float?
            by derivedStateOf { railLayoutCoor?.boundInWindow?.height }

    override var thumbLayoutCoor: P6LayoutCoor? by thumbLayoutCoorMs

    override var railLayoutCoor: P6LayoutCoor? by railLayoutCoorMs

    override val thumbPositionInPx: Float by derivedStateOf {
        (railLengthPx ?: 0f) * thumbPositionRatio
    }

    val thumbLengthInPx:Float by derivedStateOf {
        (railLengthPx?:0f) * _thumbLengthRatio
    }

    override fun computeThumbLength(density: Density): Dp {
        return with(density) { thumbLengthInPx.toDp() }
    }

    override fun setThumbLengthRatio(ratio: Float) {
        _thumbLengthRatio = ratio
    }

    /**
     * When thumb is dragged by [dragDelta] px, change the offset ratio of the thumb so that it follow the pointer.
     * Impose a cap on thumb offset ratio so that it remains within [0,1].
     */
    fun computeThumbOffsetWhenDrag(dragDelta: Float) {
        val newThumbPositionRatio = railLengthPx?.let { rl ->
            val newThumbYPx = max((thumbLayoutCoor?.boundInWindow?.top ?: 0f) + dragDelta, 0f)
            (newThumbYPx) / (rl)
        }?.coerceIn(0f, 1f) ?: 0f

        thumbPositionRatioMs.value = newThumbPositionRatio
    }

    /**
     * Shorten thumb length and move it back up when thumb reaches rail bottom
     */
    fun recomputeStateWhenThumbReachRailBottom() {

        _thumbLengthRatio = max(_thumbLengthRatio * reductionRatio, minLengthRatio)

        thumbPositionRatioMs.value = moveBackRatio

    }

    /**
     * When reach the top, reset the thumb length to the max value
     */
    fun recomputeStateWhenThumbReachRailTop() {
        _thumbLengthRatio = maxLengthRatio
    }

    override fun recomputeStateWhenThumbIsDragged(delta: Float) {

        computeThumbOffsetWhenDrag(delta)

        if (thumbReachRailBottom) {
            recomputeStateWhenThumbReachRailBottom()

        } else if (thumbReachRailTop) {
            recomputeStateWhenThumbReachRailTop()
        }
    }

    override val thumbPositionRatio: Float by thumbPositionRatioMs

    override fun computePositionRatioOnFullRail(yPx: Float): Float? {
        return computePositionRatioWithOffset(yPx, 0f)
    }

    override fun performMoveThumbWhenClickOnRail(point: Float) {
        guardFloat01(point, "percent")
        if(point>=thumbPositionRatio){
            moveThumbByPercent(0.1f)
        }else{
            moveThumbByPercent(-0.1f)
        }
    }

    fun moveThumbByPercent(percent: Float) {
        val newRatio = (thumbPositionRatio + percent).coerceIn(0f,1f)
        thumbPositionRatioMs.value = newRatio
    }


    fun computePositionRatioWithOffset(yPx: Float, offset: Float): Float? {
        val ratio =
            railLayoutCoor?.boundInWindow?.bottom?.let { railBotY ->
                if (railBotY != 0f) {
                    (yPx - offset) / (railBotY - offset)
                } else {
                    null
                }
            }

        val rt = ratio?.coerceIn(0f, 1f)

        return rt
    }


    /**
     * if the thumb has reached the bottom of the rail or not.
     * A thumb is considered "reach rail bottom" if its bottom edge touch the bottom edge of the rail
     */
    override val thumbReachRailBottom: Boolean
        get() {
            val thumbYBottom = thumbLayoutCoor?.boundInWindow?.bottom
            val railYBottom = railLayoutCoor?.boundInWindow?.bottom
            val rt = thumbYBottom != null && railYBottom != null && thumbYBottom == railYBottom
            return rt
        }

    /**
     * Tell if the thumb has reached the top of the rail or not.
     * A thumb is considered "reach rail top" if its top edge touch the top edge of the rail
     */
    override val thumbReachRailTop: Boolean
        get() {
            val thumbYTop = thumbLayoutCoor?.boundInWindow?.top
            val railYTop = railLayoutCoor?.boundInWindow?.top
            val rt = railYTop != null && thumbYTop != null && railYTop == thumbYTop
            return rt
        }
}