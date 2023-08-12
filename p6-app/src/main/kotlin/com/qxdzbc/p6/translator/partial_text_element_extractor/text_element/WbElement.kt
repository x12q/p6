package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import java.nio.file.Path

/**
 * A [TextElement] representing a workbook key.
 * [fullText] is the full text of the wb name + path. This may contain sing quote, and a path.
 * [wbName] is the name of the corresponding workbook. [wbPath] is the file path text of the workbook. These two can be used to locate a workbook.
 * Both [wbName] & [wbPath] are parts of [fullText].
 */
data class WbElement(
    val fullText: String,
    val wbName: String,
    val wbPath: String?,
    override val textRange: IntRange
) : TextElement {

    override val text: String get() = fullText

    fun toBasicTextElement(): BasicTextElement {
        return BasicTextElement(fullText, textRange)
    }

    fun toWbKey(): WorkbookKey {
        return WorkbookKey(wbName, wbPath?.let { Path.of(it) })
    }
}