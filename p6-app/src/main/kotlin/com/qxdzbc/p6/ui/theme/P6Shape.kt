package com.qxdzbc.p6.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object P6Shape{
    val stdBoxShape:Shape = RoundedCornerShape(3.dp)
    val textFieldShape:Shape = stdBoxShape
    val buttonShape:Shape = stdBoxShape

    val local = staticCompositionLocalOf(
        defaultFactory = {
            P6Shape
        }
    )
}