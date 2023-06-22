package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import java.nio.file.Path

/**
 * A [TextElement] representing a workbook key
 */
data class WbElement(
    val label:String,
    val wbName:String,
    val wbPath:String?,
    override val textRange: IntRange
): TextElement {
    override val text: String get()=label
    fun toWbKey(): WorkbookKey {
        return WorkbookKey(wbName, wbPath?.let { Path.of(it) })
    }
}