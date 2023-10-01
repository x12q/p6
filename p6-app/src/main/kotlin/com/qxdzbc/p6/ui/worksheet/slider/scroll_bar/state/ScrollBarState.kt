package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.OnDragThumbData

/**
 * This state consist of the state of a rail, and a thumb of an edge slider
 */
sealed interface ScrollBarState : ThumbPositionConverter{

    val type:ScrollBarType

    val onDragData:OnDragThumbData

    /**
     * rail length in px
     */
    val railLengthPx:Float?

    /**
     * Layout of the thumb in this edge slider
     */
    var thumbLayoutCoor:P6LayoutCoor?

    /**
     * Layout of the rail in this edge slider
     */
    var railLayoutCoor:P6LayoutCoor?

    /**
     * compute thumb length relative to rail length
     */
    fun computeThumbLength(density: Density):Dp

    fun setThumbLengthRatio(ratio:Float)

    /**
     * Offset of the thumb from the start of the rail, in px
     */
    val thumbPositionInPx:Float

    /**
     * Recompute this state when thumb is dragged, including thumb position and thumb size.
     */
    fun recomputeStateWhenThumbIsDragged(delta: Float,allowRecomputationWhenReachBot:Boolean)

    /**
     * if the thumb has reached the bottom of the rail or not.
     * A thumb is considered "reach rail bottom" if its bottom edge touch the bottom edge of the rail
     */
    val thumbReachRailEnd:Boolean

    /**
     * Tell if the thumb has reached the top of the rail or not.
     * A thumb is considered "reach rail top" if its top edge touch the top edge of the rail
     */
    val thumbReachRailStart: Boolean

    /**
     * This tells how far the thumb is away from the top of the rail in percentage (%).
     * - 0.0 (or 0%) means the thumb is at the top.
     * - 1.0 (or 100%) means the thumb is at the bottom.
     * [thumbPositionRatio] is for displaying thumb on the view layer
     * * [thumbPositionRatio] is always in [0,1] range.
     */
    val thumbPositionRatio: Float

    /**
     * this tells the actual % of the scroll.
     */
    val thumbScrollRatio:Float

    /**
     * Compute the position ratio of a point with offset [yPx] from the top of the rail against the full rail length
     */
    fun computePositionRatioOnFullRail(yPx:Float):Float?

    /**
     * Perform move thumb when the rail is clicked at [point] on rail.
     * [point] is between [0,1]
     */
    fun performMoveThumbWhenClickOnRail(point:Float)

    /**
     * Compute thumb offset base on [density]
     */
    fun computeThumbOffset(density: Density): DpOffset



    val effectiveRailLengthPx: Float?
}