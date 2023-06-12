package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult

data class ErrTextElement(
    override val text:String,
    override val range:IntRange
) :TextElement{
    fun toResult(): TextElementResult {
        return TextElementResult(
            errs = listOf(this)
        )
    }
}