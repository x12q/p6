package com.qxdzbc.common.compose

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutImp


object LayoutCoorsUtils{

    fun LayoutCoordinates.toP6Layout(previousForceRefreshVar:Boolean?=true): P6Layout {
        return P6LayoutImp(this,!(previousForceRefreshVar?:true))
    }

    fun LayoutCoordinates.toP6Layout(prevLayout:P6Layout?): P6Layout {
        return P6LayoutImp(this,!(prevLayout?.refreshVar?:true))
    }

    fun <T> LayoutCoordinates?.ifAttached(f:(lc:LayoutCoordinates)->T):T? {
        if (this != null && this.isAttached) {
            return f(this)
        }else{
            return null
        }
    }
}
