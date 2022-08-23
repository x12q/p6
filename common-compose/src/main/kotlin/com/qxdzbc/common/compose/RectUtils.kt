package com.qxdzbc.common.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

object RectUtils {
    fun makeRect(point1:Offset,point2:Offset): Rect {
        return Rect(
            topLeft = Offset(
                x= minOf(point1.x,point2.x),
                y= minOf(point1.y,point2.y)
            ),
            bottomRight =Offset(
                x= maxOf(point1.x,point2.x),
                y= maxOf(point1.y,point2.y)
            )
        )
    }
}
