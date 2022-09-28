package com.qxdzbc.common.compose.layout_coor_wrapper

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
    val isAttached:Boolean
    fun ifAttached(f:(lc: LayoutCoorWrapper)->Unit)
    @Composable
    fun ifAttachedComposable(f:@Composable (lc: LayoutCoorWrapper)->Unit)


    val refreshVar:Boolean
    /**
     * [layout] is sometimes mutated directly by compose runtime. This mean re-setting layout may or may not cause re-composition. To force re-composition, call this function with [i] being the opposite of [refreshVar] of this wrapper or the wrapper to be replaced by this wrapper.
     */
    fun forceRefresh(i:Boolean):LayoutCoorWrapper
}
