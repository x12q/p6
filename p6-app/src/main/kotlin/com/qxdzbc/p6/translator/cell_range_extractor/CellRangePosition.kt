package com.qxdzbc.p6.translator.cell_range_extractor

import org.antlr.v4.runtime.ParserRuleContext


data class CellRangePosition(
    val cellRangeLabel:String,
    val labelLoc:String? = null,
    val start:TokenPosition,
    val stop:TokenPosition
){
    companion object{
        fun from(ruleContext: ParserRuleContext):CellRangePosition{
            val c = ruleContext
            return CellRangePosition(
                cellRangeLabel =  c.text ?: "",
                start = TokenPosition(
                    charIndex = c.start.startIndex
                ),
                stop = TokenPosition(
                    charIndex = c.stop.stopIndex
                )
            )
        }
    }

    fun contains(i:Int):Boolean{
        return i >= start.charIndex && i <= stop.charIndex
    }
    fun iRange():IntRange{
        return start.charIndex .. stop.charIndex
    }
}
