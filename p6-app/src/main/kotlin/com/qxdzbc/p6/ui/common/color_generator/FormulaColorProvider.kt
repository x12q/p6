package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color

/**
 * Provide a fixed number of colors for coloring formulas
 */
interface FormulaColorProvider {
    fun getColors(count:Int):List<Color>
}
