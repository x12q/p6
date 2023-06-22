package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

/**
 * A basic [TextElement], simply contains a [text] and a [textRange].
 */
data class BasicTextElement (
    override val text:String,
    override val textRange:IntRange
): TextElement{

    constructor(text:String,i:Int):this(text,i .. i)
    constructor(text:String, from:Int, to:Int):this(text, from .. to)

    companion object{
        val empty = BasicTextElement("",-1 .. -1)
        fun from(ruleContext: ParserRuleContext): BasicTextElement {
            if(ruleContext.start.startIndex < 0 || ruleContext.stop.stopIndex < 0){
                throw IllegalStateException("Can't create BasicTextElement from ctx that have negative index")
            }
            return BasicTextElement(
                text = ruleContext.text ?: "",
                textRange = ruleContext.start.startIndex..ruleContext.stop.stopIndex
            )
        }
        fun from(token:Token):BasicTextElement{
            if(token.startIndex <0 || token.stopIndex<0){
                throw IllegalStateException("Can't create BasicTextElement from tokens that have negative index")
            }
            return BasicTextElement(text = token.text, textRange = token.startIndex..token.stopIndex)
        }
    }

    fun toResult():TextElementResult{
        return TextElementResult(
            basicTexts = listOf(this)
        )
    }
    fun toErrResult():TextElementResult{
        return TextElementResult(
            inclusiveErrs = listOf(this)
        )
    }
}
