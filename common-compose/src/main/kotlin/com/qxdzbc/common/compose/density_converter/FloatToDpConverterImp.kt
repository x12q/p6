package com.qxdzbc.common.compose.density_converter

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

class FloatToDpConverterImp(
    val density: Density
) : FloatToDpConverter {

    override fun toDp(float: Float): Dp {
        return with(density){
            float.toDp()
        }
    }
}