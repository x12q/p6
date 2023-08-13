package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

interface VerticalEdgeSliderState {
    /**
     * length of slider thumb
     */
    fun thumbLength(railLength:Dp):Dp
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

}