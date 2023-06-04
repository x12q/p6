package com.qxdzbc.common.compose.density_converter

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

class PixelToDpConverterImp(val density: Density) : PixelToDpConverter {
    override fun toDp(pixel: Int): Dp {
        return with(density){
            pixel.toDp()
        }
    }
}