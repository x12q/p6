package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import java.nio.file.Path

/**
 * A [TextElement] representing a worksheet name
 */
data class WsNameElement(
    val label: String,
    val wsName: String, override val range: IntRange,
) : TextElement {
    override val text: String = label
}

/**
 * A [TextElement] representing a workbook key
 */
data class WbElement(
    val label:String,
    val wbName:String,
    val wbPath:String?,
    override val range: IntRange
):TextElement{
    override val text: String get()=label
    fun toWbKey():WorkbookKey{
        return WorkbookKey(wbName, wbPath?.let{Path.of(it)})
    }
}

/**
 * A [TextElement] containing a cell or range address
 */
data class CellRangeElement(
    val rangeAddress: BasicTextElement,
    val wsSuffix: WsNameElement? = null,
    val wbSuffix: WbElement? = null,
) : TextElement {
    operator fun plus(wsNameElement: WsNameElement): CellRangeElement {
        return this.copy(wsSuffix=wsNameElement)
    }

    operator fun plus(wbElement: WbElement): CellRangeElement {
        return this.copy(wbSuffix=wbElement)
    }

    constructor(
        cellRangeLabel: String,
        start: Int,
        stop: Int,
    ) : this(
        rangeAddress = BasicTextElement(cellRangeLabel, start, stop)
    )

    val cellRangeLabel: String get() = rangeAddress.text
    val cellRangeSuffix: String?
        get() {
            val wsSf = wsSuffix?.text
            val wbSf = wbSuffix?.text
            if (wsSf != null) {
                if (wbSf != null) {
                    return "$wsSf$wbSf"
                } else {
                    return wsSf
                }
            } else {
                return null
            }
        }
    val startTP: TokenPosition get() = TokenPosition(rangeAddress.start)
    val stopTP: TokenPosition
        get() {
            val rt = listOfNotNull(rangeAddress.stop, wsSuffix?.stop, wbSuffix?.stop).maxBy { it }
            return TokenPosition(rt)
        }

    fun contains(i: Int): Boolean {
        return i >= startTP.charIndex && i <= stopTP.charIndex
    }

    fun iRange(): IntRange {
        return range
    }

    override val text: String
        get() = cellRangeLabel + (this.cellRangeSuffix ?: "")
    override val range: IntRange
        get() = this.startTP.charIndex..this.stopTP.charIndex
}
