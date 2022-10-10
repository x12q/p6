package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color

/**
 * Provide a fixed number of colors on request. Color from this class is for coloring formula only. Don't use it for any other purpose.
 */
interface MultiColorGenerator {
    fun getColors(count:Int):List<Color>
}
