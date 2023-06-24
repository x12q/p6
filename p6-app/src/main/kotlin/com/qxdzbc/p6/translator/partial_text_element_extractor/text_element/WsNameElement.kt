package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

/**
 * A [TextElement] representing a worksheet name.
 * [fullText] is the full text of the ws name. This may contains single quote
 * [wsName] just the worksheet name, can be used for looking up worksheet. [wsName] is always part of the [fullText]
 */
data class WsNameElement(
    val fullText: String,
    val wsName: String,
    override val textRange: IntRange,
) : TextElement {

    override val text: String = fullText

    fun toBasicTextElement():BasicTextElement{
        return BasicTextElement(fullText,textRange)
    }
}