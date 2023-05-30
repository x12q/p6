package com.qxdzbc.common.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.SizeUtils.toDpSize

object SizeUtils{
    /**
     * This is inherently wrong. Without a density, it is not possible to convert pixel to dp size reliably. But somehow it works on Linux.
     */
    @Deprecated("wrong logic, don't use")
    fun IntSize.toDpSize(): DpSize {
        return DpSize(this.width.dp, this.height.dp)
    }

    fun IntSize.toDpSize(density: Density): DpSize {
        val rt = with(density){
            DpSize(width.toDp(), height.toDp())
        }
        return rt
    }

    /**
     * This is good because
     */
    fun Size.toDpSize(): DpSize {
        return DpSize(this.width.dp, this.height.dp)
    }
}
