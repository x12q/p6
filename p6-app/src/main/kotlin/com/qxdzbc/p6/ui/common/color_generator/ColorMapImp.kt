package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.translator.formula.execution_unit.ColorKey

class ColorMapImp(
    private val colorMap: Map<ColorKey, Color>
) : ColorMap {

    constructor(
        colorKeys: List<ColorKey>,
        colors: List<Color>
    ) : this(
        colorMap = colors.withIndex().associateBy(
            keySelector = { (i, c) -> colorKeys[i] },
            valueTransform = { (i, c) -> c }
        )
    ){
        require(colorKeys.size == colors.size){
            "colorKeys list must have the same number of element as the colors list"
        }
    }

    override fun getColor(colorKey: ColorKey): Color? {
        return colorMap[colorKey]
    }
}
