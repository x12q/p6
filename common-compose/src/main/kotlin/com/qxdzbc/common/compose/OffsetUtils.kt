package com.qxdzbc.common.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
object OffsetUtils{
    fun Offset.negate(): Offset {
        return Offset(-this.x,-this.y)
    }
    fun Offset.toIntOffset(): IntOffset {
        return IntOffset(this.x.toInt(), this.y.toInt())
    }

}
