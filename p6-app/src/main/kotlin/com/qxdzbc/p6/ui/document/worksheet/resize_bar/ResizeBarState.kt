package com.qxdzbc.p6.ui.document.worksheet.resize_bar

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType

interface ResizeBarState {
    val size: Dp
    val selectableAreaWidth: Dp
    val isShowThumb:Boolean
    fun showThumb():ResizeBarState
    fun hideThumb():ResizeBarState

    val isShow:Boolean

    fun show():ResizeBarState
    fun hide():ResizeBarState

    val isActive:Boolean
    fun activate():ResizeBarState
    fun deactivate():ResizeBarState

    val anchorPointOffset:Offset
    val thickness: Dp
    val offset:Offset
    val dimen:RulerType
    fun changePosition(newPosition:Offset):ResizeBarState

    fun setAnchor(anchorPoint:Offset):ResizeBarState
}


