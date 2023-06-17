package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult

object EOFTextElement : TextElement {
    override val text: String
        get() = "<EOF>"
    override val range: IntRange
        get() = IntRange(-1,-1)

    fun toResult():TextElementResult{
        return TextElementResult()
    }
}