package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

/**
 * Represent a string and its position in a larger string.
 *
 * @property range is the index range that this text reside inside its parent text
 */
sealed interface TextElement{
    val text:String
    val range:IntRange
    val start:Int get()=range.first
    val stop:Int get()=range.last
}
