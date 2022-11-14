package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

data class OtherElement constructor(
    override val text:String,
    override val range:IntRange
): TextElement{

    constructor(text:String,i:Int):this(text,i .. i)

    companion object{
        val empty = OtherElement("",-1 .. -1)
        fun from(ruleContext: ParserRuleContext): OtherElement {
            val c = ruleContext
            return OtherElement(
                text =  c.text ?: "",
                range = c.start.startIndex .. c.stop.stopIndex
            )
        }
        fun from(token:Token):OtherElement{
            return OtherElement(text = token.text, range = token.startIndex..token.stopIndex)
        }
    }

    fun toResult():TextElementResult{
        return TextElementResult(
            others = listOf(this)
        )
    }
}
