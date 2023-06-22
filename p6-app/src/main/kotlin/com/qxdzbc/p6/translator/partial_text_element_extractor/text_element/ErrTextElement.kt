package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import org.antlr.v4.runtime.Token

data class ErrTextElement(
    override val text: String,
    override val textRange: IntRange,
    val exception: Exception? = null,
) : TextElement {

    constructor(text: String, from: Int, to: Int) : this(
        text = text,
        textRange = from..to,
        exception = null,
    )

    fun toResult(): TextElementResult {
        return TextElementResult(
            errs = listOf(this)
        )
    }

    companion object {
        fun fromException(exception: Exception): ErrTextElement {
            return ErrTextElement(
                text = "",
                textRange = IntRange(-1, -1),
                exception = exception,
            )
        }

        fun from(token: Token): ErrTextElement {
            return ErrTextElement(
                text = token.text,
                textRange = token.startIndex..token.stopIndex,
                exception = null,
            )
        }

        fun List<ErrTextElement>.toResult():TextElementResult{
            return TextElementResult(
                errs = this
            )
        }
    }
}