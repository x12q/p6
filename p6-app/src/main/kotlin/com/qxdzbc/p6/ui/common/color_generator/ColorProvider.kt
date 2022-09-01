package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.translator.formula.execution_unit.ColorKey

/**
 * Provide color by key
 */
interface ColorProvider {
    fun getColor(colorKey: ColorKey): Color?
}

