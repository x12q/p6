package com.qxdzbc.p6.ui.document.worksheet.select_rect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper

/**
 * A rectangle for selection
 */
interface SelectRectState {
    val isShow: Boolean
    fun show(): SelectRectState
    fun hide(): SelectRectState

    val rect: Rect
    val anchorPoint: Offset
    val movingPoint: Offset

    /**
     * active == can be resized
     */
    val isActive: Boolean
    fun setMovingPoint(point: Offset): SelectRectState
    fun setAnchorPoint(anchorPoint: Offset): SelectRectState
    fun setActiveStatus(isDown: Boolean): SelectRectState
    fun activate(): SelectRectState {
        return this.setActiveStatus(true)
    }

    fun deactivate(): SelectRectState {
        return this.setActiveStatus(false)
    }

    val isMovingDownward:Boolean
    fun isMovingDownward(ratio:Double=1.0):Boolean

    val isMovingUpward:Boolean
    fun isMovingUpward(ratio:Double=1.0):Boolean

    val isMovingToTheLeft:Boolean
    fun isMovingToTheLeft(ratio:Double=1.0):Boolean

    val isMovingToTheRight:Boolean
    fun isMovingToTheRight(ratio:Double=1.0):Boolean
}
