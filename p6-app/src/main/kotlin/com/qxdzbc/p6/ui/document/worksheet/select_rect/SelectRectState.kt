package com.qxdzbc.p6.ui.document.worksheet.select_rect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

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
}
