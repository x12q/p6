package com.emeraldblast.p6.ui.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.positionInWindow

fun LayoutCoordinates.wrap():LayoutCoorWrapper{
    return LayoutCoorWrapperImp(this)
}
object LayoutCoorsFunctions{
    fun LayoutCoordinates?.ifAttached(f:(lc:LayoutCoordinates)->Unit) {
        if (this != null && this.isAttached) {
            f(this)
        }
    }
    @Composable
    fun LayoutCoordinates?.ifAttachedComposable(f:@Composable (lc:LayoutCoordinates) ->Unit) {
        if (this != null && this.isAttached) {
            f(this)
        }
    }
}
