package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.translator.formula.execution_unit.ColorKey

class ColorProviderImp(
    private val colorMap: Map<ColorKey, Color>
) : ColorProvider {

    constructor(
        colorKeys: List<ColorKey>,
        colors: List<Color>
    ) : this(
        colors.withIndex().associateBy(
            keySelector = { (i, c) -> colorKeys[i] },
            valueTransform = { (i, c) -> c }
        )
    )

    override fun getColor(colorKey: ColorKey): Color? {
        return colorMap[colorKey]
    }
}
