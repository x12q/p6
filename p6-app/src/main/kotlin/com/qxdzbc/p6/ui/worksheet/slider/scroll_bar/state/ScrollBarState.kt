package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.OnDragThumbData

/**
 * This state consist of the state of a rail, and a thumb of an edge slider
 */
sealed interface ScrollBarState : ThumbPositionConverter {

    val thumbLengthRatio: Float
    val type: ScrollBarType

    val onDragData: OnDragThumbData

    /**
     * Layout of the rail in this edge slider
     */
    var railLayoutCoor: P6Layout?

    /**
     * the real rail length in px
     */
    val railLengthPx: Float?

    /**
     * Effective rail length is the rail length minus the thumb length. This number is used to compute the [effectiveThumbPositionRatio].
     */
    val effectiveRailLengthPx: Float?

    /**
     * Layout of the thumb in this edge slider
     */
    var thumbLayoutCoor: P6Layout?

    /**
     * Offset of the start of the thumb from the start of the rail, in px
     */
    val thumbPositionInPx: Float

    /**
     * This tells how far the top of the thumb is away from the top of the rail in percentage (%).
     * - 0.0 (or 0%) means the thumb is at the top.
     * - 1.0 (or 100%) means the thumb is at the bottom.
     * * [thumbPositionRatio] is for displaying thumb on the view layer
     * * [thumbPositionRatio] is always in [0,1] range.
     * * [thumbPositionRatio] can never reach 1 (or 100%) because the top of the thumb can never reach the bot of the rail.
     */
    val thumbPositionRatio: Float

    /**
     * this tells the effective % of the thumb position. This is not a real number, but one computed from thumb real position, thumb length, and rail length.
     * This number is between [0,1] with 0 mean the thumb is at the start of the rail, and 1 means the thumb is at the end of the rail. This is called "effective" because the real thumb position can never reach 1.
     * The purpose of this number is to give caller a basis to determine the semantic poisition of the thumb. So, caller should use this number instead of [thumbPositionRatio]
     */
    val effectiveThumbPositionRatio: Float


    /**
     * compute thumb length as dp (base on [density]) relative to rail length
     */
    fun computeThumbLength(density: Density): Dp

    fun setThumbLengthRatio(ratio: Float)

    /**
     * Set [thumbPositionRatio] by derive a new [thumbPositionRatio] from an [effectivePositionRatio]
     */
    fun setThumbPositionRatioViaEffectivePositionRatio(effectivePositionRatio: Float)

    /**
     * Recompute this state when thumb is dragged, including thumb position and thumb size.
     * [allowRecomputationWhenReachBot] means allow recomputing the scroll bar when the thumb reach the bottom of the rail or not. Normally, thumb size and position would be recomputed to allow infinite scrolling, but when the hosting worksheet reach its col and row limit, the re-computation is no longer needed.
     */
    fun recomputeStateWhenThumbIsDragged(delta: Float)

    /**
     * if the thumb has reached the bottom of the rail or not.
     * A thumb is considered "reach rail bottom" if its bottom edge touch the bottom edge of the rail
     */
    val thumbReachRailEnd: Boolean

    /**
     * Tell if the thumb has reached the top of the rail or not.
     * A thumb is considered "reach rail top" if its top edge touch the top edge of the rail
     */
    val thumbReachRailStart: Boolean



    /**
     * Compute the position ratio of a point with offset [yPx] from the top of the rail against the full rail length
     */
    fun computePositionRatioOnFullRail(yPx: Float): Float?

    /**
     * Perform move thumb when the rail is clicked at [point] on rail.
     * [point] is between [0,1]
     */
    fun performMoveThumbWhenClickOnRail(point: Float)

    /**
     * Compute thumb offset base on [density]
     */
    fun computeThumbOffset(density: Density): DpOffset

    /**
     * recomputes the thumb state when the thumb is released from drag
     */
    @Deprecated("for now only, must be replaced by a version that use value from slider")
    fun naiveRecomputeThumbStateWhenThumbIsReleasedFromDrag()

    /**
     * TWO task:
     * T2: when I scroll the gridSlider, I must move and resize the thumb as well
     *  - move: by how much
     *  - resize: by how much
     *      - grid slider must have access to scrollbar state to mutate scroll bar state
     *
     */

    fun resetThumbLength()
    fun resetThumbPosition()
}