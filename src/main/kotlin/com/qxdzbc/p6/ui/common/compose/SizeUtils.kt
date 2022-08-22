package com.qxdzbc.p6.ui.common.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

object SizeUtils{
    fun IntSize.toDpSize(): DpSize {
        return DpSize(this.width.dp, this.height.dp)
    }

    fun Size.toDpSize(): DpSize {
        return DpSize(this.width.dp, this.height.dp)
    }
}
