package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color

/**
 * Provide a fixed number of colors on request.
 */
interface MultiColorGenerator {
    fun getColors(count:Int):List<Color>
}
