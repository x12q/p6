package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color
import javax.inject.Inject

/**
 * a [MultiColorGenerator] for generating color for coloring formula. This implementation keeps a cache of generated colors and used them in subsequent requests.
 * TODO a safety limit should be set so that only a limited number of colors are stored to prevent over-memory-consumption. Colors pass that limit are generated on-demand and not stored. Must ensure on-demand color are always the same.
 */

class FormulaColorGeneratorImp @Inject constructor(
    private val colorGenerator: ColorGenerator
) : FormulaColorGenerator {
    private var cacheColors:List<Color> = emptyList()
    override fun getColors(count: Int): List<Color> {
        if(count > cacheColors.size){
            val dif = count - cacheColors.size
            for(x in 1 .. dif){
                cacheColors = cacheColors + colorGenerator.nextColor()
            }
        }
        return cacheColors.subList(0,count)
    }
}
