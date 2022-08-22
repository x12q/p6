package com.emeraldblast.p6.ui.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.ui.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.layout_coor_wrapper.LayoutCoorWrapperImp


object LayoutCoorsUtils{
    fun LayoutCoordinates.wrap(): LayoutCoorWrapper {
        return LayoutCoorWrapperImp(this)
    }
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
