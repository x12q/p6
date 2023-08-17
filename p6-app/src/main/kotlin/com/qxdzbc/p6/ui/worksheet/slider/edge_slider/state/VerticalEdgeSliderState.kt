package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor

interface VerticalEdgeSliderState {

    /**
     * rail length in px
     */
    val railLengthPx:Float?

    var thumbLayoutCoor:P6LayoutCoor?
    var railLayoutCoor:P6LayoutCoor?

    /**
     * compute thumb length relative to rail length
     */
    fun computeRelativeThumbLength(railLength:Dp):Dp

    fun setThumbLengthRatio(ratio:Float)

    /**
     * Offset in comparison to slider rail
     */
    var thumbPosition:DpOffset

    /**
     * Compute new [thumbPosition] with a [density] and a [dragDelta].
     * [dragDelta] is produced by drag modifier.
     */
    fun setThumbOffsetWhenDrag(density: Density, dragDelta: Float, railLength: Dp)

    /**
     * recompute this state when thumb reach the bottom of the rail
     */
    fun recomputeStateWhenThumbReachRailBottom(railLength: Dp)

    /**
     * recompute this state when thumb reach the top of the rail
     */
    fun recomputeStateWhenThumbReachRailTop()

    /**
     * Recompute states when thumb is dragged. This also affect the grid slider state attached to this state
     */
    fun recomputeStateWhenThumbIsDragged(density: Density, delta: Float)

    /**
     * if the thumb has real the bottom of the rail or not.
     * A thumb is considered "reach rail bottom" if its bottom edge touch the bottom edge of the rail
     */
    val thumbReachRailBottom:Boolean

    /**
     * if the thumb has real the top of the rail or not.
     * A thumb is considered "reach rail top" if its top edge touch the top edge of the rail
     */
    val thumbReachRailTop: Boolean

    /**
     * [slideRatio] tells how far the thumb is away from the top of the rail in percentage (%).
     * - 0.0 (or 0%) means the thumb is at the top.
     * - 1.0 (or 100%) means the thumb is at the bottom
     * [slideRatio] is always in [0,1] range
     */
    val slideRatio: Float?
}