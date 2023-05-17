package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.translator.formula.execution_unit.ColorKey

/**
 * a map of colors. This is for caching color.
 */
interface ColorMap {
    fun getColor(colorKey: ColorKey): Color?
}

