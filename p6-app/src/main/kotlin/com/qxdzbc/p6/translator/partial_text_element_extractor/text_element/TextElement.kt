package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

/**
 * Represent a piece of string and its position in a larger string.
 *
 *  [range] is the index range that this text reside inside its parent text. It is noted that this range is inclusive. That means both of its ends are index of the actual [text]. This range is different from normal range used in substring, don't use it for substring, use [rangeForSubStr] instead.
 */
sealed interface TextElement{
    val text:String
    val range:IntRange
    val start:Int get()=range.first
    val stop:Int get()=range.last
    val stopForSubStr:Int get()=stop+1
    val rangeForSubStr:IntRange get()= start .. stop+1
}
