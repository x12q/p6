package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color

interface FormulaColorProvider {
    fun getColors(count:Int):List<Color>
}
