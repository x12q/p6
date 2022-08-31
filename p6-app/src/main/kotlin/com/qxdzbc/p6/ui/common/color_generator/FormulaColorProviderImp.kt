package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color
import javax.inject.Inject

class FormulaColorProviderImp @Inject constructor(
    val colorGenerator: ColorGenerator
) : FormulaColorProvider {
    private var colors:List<Color> = emptyList()
    override fun getColors(count: Int): List<Color> {
        if(count > colors.size){
            val dif = count - colors.size
            for(x in 1 .. dif){
                colors = colors + colorGenerator.nextColor()
            }
        }
        return colors.subList(0,count)
    }
}
