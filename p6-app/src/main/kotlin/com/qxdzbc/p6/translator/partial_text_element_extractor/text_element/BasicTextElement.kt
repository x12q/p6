package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

data class BasicTextElement constructor(
    override val text:String,
    override val range:IntRange
): TextElement{

    constructor(text:String,i:Int):this(text,i .. i)
    constructor(text:String, from:Int, to:Int):this(text, from .. to)

    companion object{
        val empty = BasicTextElement("",-1 .. -1)
        fun from(ruleContext: ParserRuleContext): BasicTextElement {
            val c = ruleContext
            return BasicTextElement(
                text =  c.text ?: "",
                range = c.start.startIndex .. c.stop.stopIndex
            )
        }
        fun from(token:Token):BasicTextElement{
            return BasicTextElement(text = token.text, range = token.startIndex..token.stopIndex)
        }
    }

    fun toResult():TextElementResult{
        return TextElementResult(
            others = listOf(this)
        )
    }
}
