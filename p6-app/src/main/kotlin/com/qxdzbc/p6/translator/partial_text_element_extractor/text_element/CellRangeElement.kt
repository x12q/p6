package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import org.antlr.v4.runtime.ParserRuleContext


data class CellRangeElement(
    val cellRangeLabel:String,
    val labelLoc:String? = null,
    val startTP: TokenPosition,
    val stopTP: TokenPosition
): TextElement {
    companion object{
        fun from(ruleContext: ParserRuleContext): CellRangeElement {
            val c = ruleContext
            return CellRangeElement(
                cellRangeLabel =  c.text ?: "",
                startTP = TokenPosition(
                    charIndex = c.start.startIndex
                ),
                stopTP = TokenPosition(
                    charIndex = c.stop.stopIndex
                )
            )
        }
    }

    fun contains(i:Int):Boolean{
        return i >= startTP.charIndex && i <= stopTP.charIndex
    }
    fun iRange():IntRange{
        return range
    }

    override val text: String
        get() = cellRangeLabel + (this.labelLoc?:"")
    override val range: IntRange
        get() = this.startTP.charIndex .. this.stopTP.charIndex
}
