package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

sealed interface TextElement{
    val text:String
    val range:IntRange
    val start:Int get()=range.first
    val stop:Int get()=range.last

}

