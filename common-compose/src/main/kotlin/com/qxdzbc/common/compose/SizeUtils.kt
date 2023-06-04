package com.qxdzbc.common.compose

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

object SizeUtils{
    fun IntSize.toDpSize(density: Density): DpSize {
        val rt = with(density){
            DpSize(width.toDp(), height.toDp())
        }
        return rt
    }
}
