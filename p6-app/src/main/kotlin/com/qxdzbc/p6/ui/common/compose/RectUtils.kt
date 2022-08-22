package com.qxdzbc.p6.ui.common.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

object RectUtils {
    fun makeRect(point1:Offset,point2:Offset): Rect {
        return com.qxdzbc.p6.common.compose.RectUtils.makeRect(point1,point2)
//        return Rect(
//            topLeft = Offset(
//                x= minOf(point1.x,point2.x),
//                y= minOf(point1.y,point2.y)
//            ),
//            bottomRight =Offset(
//                x= maxOf(point1.x,point2.x),
//                y= maxOf(point1.y,point2.y)
//            )
//        )
    }
}
