package com.qxdzbc.p6.ui.document.worksheet.resize_bar

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType

interface ResizeBarState {
    val size:Int
    val selectableAreaWidth:Int
    val isShowThumb:Boolean
    fun showThumb():ResizeBarState
    fun hideThumb():ResizeBarState

    val isShow:Boolean

    fun show():ResizeBarState
    fun hide():ResizeBarState

    val isActive:Boolean
    fun activate():ResizeBarState
    fun deactivate():ResizeBarState

    val anchorPoint:Offset
    val thickness: Int
    val position:Offset
    val dimen:RulerType
    fun changePosition(newPosition:Offset):ResizeBarState

    fun setAnchor(anchorPoint:Offset):ResizeBarState
}


