package com.qxdzbc.common.compose.density_converter

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun interface FloatToDpConverter {
    fun toDp(float:Float):Dp
}

fun FloatToDpConverter(density: Density):FloatToDpConverter{
    return FloatToDpConverterImp(density)
}

fun interface PixelToDpConverter{
    fun toDp(pixel:Int):Dp
}

fun PixelToDpConverter(density: Density):PixelToDpConverter{
    return PixelToDpConverterImp(density)
}