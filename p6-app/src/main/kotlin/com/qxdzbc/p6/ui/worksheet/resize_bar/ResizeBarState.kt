package com.qxdzbc.p6.ui.worksheet.resize_bar

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.qxdzbc.p6.ui.worksheet.ruler.RulerType

/**
 * State of a [ResizeBar].
 */
interface ResizeBarState {

    /**
     * Size of the [ResizeBar]'s thumb. Not the thickness
     * - for column: this will be its height
     * - for row: this will be its width.
     */
    val thumbSize: Dp

    /**
     * Size of the area in which users can activate the drag-to-resize action.
     */
    val selectableAreaWidth: Dp

    /**
     * Whether to show the resize thumb or not.
     * The resize thumb is shown when users hover their mouse over selectable area
     */
    val isShowThumb:Boolean

    /**
     * set [isShowThumb] to true
     */
    fun showThumb():ResizeBarState

    /**
     * set [isShowThumb] to false
     */
    fun hideThumb():ResizeBarState

    /**
     * Whether to show the hair-line resize bar or not.
     */
    val isShowBar:Boolean

    /**
     * set [isShowBar] to true
     */
    fun showBar():ResizeBarState

    /**
     * set [isShowBar] to false
     */
    fun hideBar():ResizeBarState

    /**
     * A resize bar is:
     * - active when user click down on its selectable area with their mouse.
     * - not active when user release clicking down.
     */
    val isActive:Boolean

    /**
     * set [isActive] to true
     */
    fun activate():ResizeBarState

    /**
     * set [isActive] to false
     */
    fun deactivate():ResizeBarState

    /**
     * This is the position of the point where users click down on the selectable area.
     * It can be set by [setAnchorPointOffset].
     * This is a local offset because views can only offset relative to their parent view.
     */
    val anchorPointOffset:Offset

    fun setAnchorPointOffset(i:Offset):ResizeBarState

    val resizeBarThickness: Dp

    /**
     * This is the offset of the resize bar from [anchorPointOffset]
     */
    val resizeBarOffset:Offset

    fun setResizeBarOffset(i:Offset):ResizeBarState

    /**
     * Whether this is a col or a row ruler
     */
    val rulerType:RulerType

}


