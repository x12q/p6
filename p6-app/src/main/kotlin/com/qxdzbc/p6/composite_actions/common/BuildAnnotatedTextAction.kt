package com.qxdzbc.p6.composite_actions.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TextElement

interface BuildAnnotatedTextAction {
    /**
     * Build an [AnnotatedString] from a list of [TextElement] and [SpanStyle]
     */
    fun buildAnnotatedText(textElements:List<TextElement>, spans:List<SpanStyle>):AnnotatedString
}
