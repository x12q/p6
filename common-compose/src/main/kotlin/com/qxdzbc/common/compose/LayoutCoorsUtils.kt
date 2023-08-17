package com.qxdzbc.common.compose

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoorImp


object LayoutCoorsUtils{

    fun LayoutCoordinates.wrap(previousForceRefreshVar:Boolean?=true): P6LayoutCoor {
        return P6LayoutCoorImp(this,!(previousForceRefreshVar?:true))
    }

    fun <T> LayoutCoordinates?.ifAttached(f:(lc:LayoutCoordinates)->T):T? {
        if (this != null && this.isAttached) {
            return f(this)
        }else{
            return null
        }
    }
}
