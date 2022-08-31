package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.translator.formula.execution_unit.ColorKey
import javax.inject.Inject

class RandomColorProvider @Inject constructor(
    private val colorGenerator: ColorGenerator
) : ColorProvider {
    override fun getColor(colorKey: ColorKey): Color {
        return colorGenerator.nextColor()
    }
}
