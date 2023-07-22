package com.qxdzbc.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapperImp


object LayoutCoorsUtils{

    fun LayoutCoordinates.wrap(): LayoutCoorWrapper {
        return LayoutCoorWrapperImp(this)
    }

    fun <T> LayoutCoordinates?.ifAttached(f:(lc:LayoutCoordinates)->T):T? {
        if (this != null && this.isAttached) {
            return f(this)
        }else{
            return null
        }
    }
}
