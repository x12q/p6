package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.qxdzbc.common.FloatUtils.guardFloat01
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.EdgeSliderConstants.moveThumbByClickRatio
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.OnDragThumbData
import kotlin.math.max
import kotlin.math.roundToInt


/**
 * This contains implementation for function that is common to both vertical and horizontal edge slider state.
 */
@WsScope
sealed class AbsEdgeSliderState(
    val thumbLengthRatioMs: Ms<Float>,
    val thumbPositionRatioMs: Ms<Float>,
    val maxLengthRatio: Float,
    val minLengthRatio: Float,
    val reductionRatio: Float,
    val moveBackRatio: Float,
    val thumbLayoutCoorMs: Ms<P6LayoutCoor?>,
    val railLayoutCoorMs: Ms<P6LayoutCoor?>,
) : EdgeSliderState {

    protected abstract val railEndInWindowPx: Float?

    protected abstract val thumbEndInWindowPx: Float?

    /**
     * starting point (offset) of the thumb in px relative to the offset of the rail.
     */
    protected abstract val thumbStartInParentPx: Float?

    private var _thumbLengthRatio by thumbLengthRatioMs

    override var thumbLayoutCoor: P6LayoutCoor? by thumbLayoutCoorMs

    override var railLayoutCoor: P6LayoutCoor? by railLayoutCoorMs

    override val thumbPositionInPx: Float by derivedStateOf {
        (railLengthPx ?: 0f) * thumbPositionRatio
    }

    val thumbLengthInPx: Float by derivedStateOf {
        (railLengthPx ?: 0f) * _thumbLengthRatio
    }

    override fun computeThumbLength(density: Density): Dp {
        return with(density) { thumbLengthInPx.toDp() }
    }

    override fun setThumbLengthRatio(ratio: Float) {
        _thumbLengthRatio = ratio
    }

    override val effectiveRailLengthPx: Float? by derivedStateOf { railLengthPx?.minus(thumbLengthInPx) }



    /**
     * When thumb is dragged by [dragDelta] px, change the offset ratio of the thumb so that it follow the pointer.
     */
    fun computeThumbOffsetWhenDrag(dragDelta: Float) {
        /**
         * thumb position ratio = thumb top offset / (rail length - thumb length).
         */
        val newThumbPositionRatio = railLengthPx?.let { rl ->
            val newThumbOffsetPx = max((thumbStartInParentPx ?: 0f) + dragDelta, 0f)
            val q = newThumbOffsetPx / rl
            q
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
     * Prevent the thumb moving pass bottom
     */
    fun stuckTheThumbAtBot() {
        val rl = railLengthPx
        if (rl != null && rl > 0f) {
            val thumbBot = thumbEndInWindowPx
            val railBot = railEndInWindowPx
            if (thumbBot != null && railBot != null) {
                if (thumbBot >= railBot) {
                    // reset thumb position ratio so that thumb bot is equal rail bot
                    val newThumbPositionRatio = (railBot - thumbLengthInPx) / rl
                    thumbPositionRatioMs.value = newThumbPositionRatio
                }
            }
        }
    }

    /**
     * When reach the top, reset the thumb length to the max value
     */
    fun recomputeStateWhenThumbReachRailTop() {
        _thumbLengthRatio = maxLengthRatio
    }

    override fun recomputeStateWhenThumbIsDragged(delta: Float, allowRecomputationWhenReachBot: Boolean) {
        computeThumbOffsetWhenDrag(delta)

        if (thumbReachRailEnd) {
            if (allowRecomputationWhenReachBot) {
                recomputeStateWhenThumbReachRailBottom()
            } else {
                if (delta > 0f) {
                    stuckTheThumbAtBot()
                }
            }
        } else if (thumbReachRailStart) {
            recomputeStateWhenThumbReachRailTop()
        }
    }

    override val thumbPositionRatio: Float by thumbPositionRatioMs

    override val thumbScrollRatio: Float get() {
        val thumbYTop = railLengthPx?.times(thumbPositionRatio)
        val effectiveRL = effectiveRailLengthPx
        if (thumbYTop != null && effectiveRL != null && effectiveRL != 0f) {
            return thumbYTop / effectiveRL
        } else {
            return 0f
        }
    }

    override fun computePositionRatioOnFullRail(yPx: Float): Float? {
        return computePositionRatioWithOffset(yPx, 0f)
    }

    override fun performMoveThumbWhenClickOnRail(point: Float) {
        guardFloat01(point, "point")
        if (point >= thumbPositionRatio) {
            moveThumbByPercent(moveThumbByClickRatio)
        } else {
            moveThumbByPercent(-moveThumbByClickRatio)
        }
    }

    fun moveThumbByPercent(percent: Float) {
        val newRatio = (thumbPositionRatio + percent).coerceIn(0f, 1f)
        thumbPositionRatioMs.value = newRatio
    }


    fun computePositionRatioWithOffset(yPx: Float, offset: Float): Float? {
        val ratio =
            railEndInWindowPx?.let { railBotY ->
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
    override val thumbReachRailEnd: Boolean get() {
        val thumbYBottom = thumbEndInWindowPx
        val railYBottom = railEndInWindowPx
        if (thumbYBottom != null && railYBottom != null) {
            return thumbYBottom >= railYBottom
        } else {
            return false
        }
    }

    protected abstract val thumbStartInWindowPx: Float?
    protected abstract val railStartInWindowPx: Float?

    /**
     * Tell if the thumb has reached the top of the rail or not.
     * A thumb is considered "reach rail top" if its top edge touch the top edge of the rail
     */
    override val thumbReachRailStart: Boolean
        get() {
            val thumbYTop = thumbStartInWindowPx
            val railYTop = railStartInWindowPx
            val rt = railYTop != null && thumbYTop != null && railYTop == thumbYTop
            return rt
        }

    override fun projectThumbPositionToIndex(indexRange: IntRange): Int {
        val range = indexRange.last - indexRange.first
        val position = (range * thumbScrollRatio).toInt()
        return position + indexRange.first
    }

    override val onDragData: OnDragThumbData by derivedStateOf {
        OnDragThumbData(
            positionRatio = thumbPositionRatio,
            scrollRatio = thumbScrollRatio
        )
    }
}