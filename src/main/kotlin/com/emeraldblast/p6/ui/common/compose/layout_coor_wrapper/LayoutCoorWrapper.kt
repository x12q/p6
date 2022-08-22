package com.emeraldblast.p6.ui.common.compose.layout_coor_wrapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.DpSize

/**
 * The purpose of this interface is to provide an easily mock-able abstraction layer,
 * so that classes relying on LayoutCoordinates can depend on this instead.
 */
interface LayoutCoorWrapper {
    val layout: LayoutCoordinates
    val size:DpSize
    val boundInWindow: Rect
    val posInWindow:Offset
    fun localToWindow(local:Offset):Offset
    fun windowToLocal(window:Offset):Offset
    fun isAttached():Boolean
    fun ifAttached(f:(lc: LayoutCoorWrapper)->Unit)
    @Composable
    fun ifAttachedComposable(f:@Composable (lc: LayoutCoorWrapper)->Unit)
}
