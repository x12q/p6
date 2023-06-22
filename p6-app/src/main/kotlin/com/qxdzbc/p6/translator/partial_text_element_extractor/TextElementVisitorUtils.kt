package com.qxdzbc.p6.translator.partial_text_element_extractor

import org.antlr.v4.runtime.Token

object TextElementVisitorUtils {
    internal fun Token.hasValidRange(): Boolean {
        return this.startIndex >= 0 && this.stopIndex >= 0
    }
}