package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import java.nio.file.Path

data class WsNameElement(
    val label: String,
    val wsName: String, override val range: IntRange,
) : TextElement {
    override val text: String = label
}

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
    @Deprecated("don't use, just here so that old code is not broken")
    constructor(
        cellRangeLabel: String,
        startTP: TokenPosition,
        stopTP: TokenPosition,
    ) : this(
        rangeAddress = BasicTextElement(cellRangeLabel, startTP.charIndex, stopTP.charIndex)
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
