package com.qxdzbc.p6.ui.document.worksheet.resize_bar

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType

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

    val selectableAreaWidth: Dp

    val isShowThumb:Boolean

    fun showThumb():ResizeBarState

    fun hideThumb():ResizeBarState

    val isShowBar:Boolean

    fun showBar():ResizeBarState

    fun hideBar():ResizeBarState

    val isActive:Boolean

    fun activate():ResizeBarState

    fun deactivate():ResizeBarState

    val anchorPointOffset:Offset

    val thickness: Dp

    val offset:Offset

    val rulerType:RulerType

    fun changePosition(newPosition:Offset):ResizeBarState

    fun setAnchor(anchorPoint:Offset):ResizeBarState
}


