package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

/**
 * A [TextElement] representing a worksheet name
 */
data class WsNameElement(
    val label: String,
    val wsName: String, override val range: IntRange,
) : TextElement {
    override val text: String = label
}