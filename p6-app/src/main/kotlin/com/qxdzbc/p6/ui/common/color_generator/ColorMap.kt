package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color

/**
 * a map of colors. This is for caching color.
 */
interface ColorMap {
    fun getColor(colorKey: ColorKey): Color?
}

