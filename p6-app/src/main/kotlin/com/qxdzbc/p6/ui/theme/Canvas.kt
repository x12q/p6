package com.qxdzbc.p6.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke

object Canvas{
    val dashLine:Stroke = Stroke(
        width = 2.0f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    val local = staticCompositionLocalOf(
        defaultFactory = {
            Canvas
        }
    )


}