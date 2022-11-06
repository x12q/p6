package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.OtherElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TextElement

data class TextElementResult(
    val cellRangeElements: List<CellRangeElement> = emptyList(),
    val others: List<OtherElement> = emptyList(),
) {
    companion object {
        fun from(i: CellRangeElement): TextElementResult {
            return TextElementResult(cellRangeElements = listOf(i))
        }

        fun from(i: OtherElement): TextElementResult {
            return TextElementResult(others = listOf(i))
        }

        val empty = TextElementResult()
    }

    fun allSorted(): List<TextElement> {
        return (cellRangeElements + others).sortedBy { it.range.first }
    }

    /**
     * scan the sorted list of element, detect elements that are not continuous (stop index of prev element != start index of the next element + 1)
     */
    fun allSortedWithPadding():List<TextElement>{
        val all = allSorted()
        val rt = mutableListOf<TextElement>()
        for((i,e) in all.withIndex()){
            rt.add(e)
            val nextE = all.getOrNull(i+1)
            if(nextE!=null){
                val nextStart = nextE.start
                val currentStop = e.stop
                val diff= nextStart - currentStop
                if(diff>1){
                    rt.add(OtherElement(
                        text = " ".repeat(diff-1),
                        range = (currentStop+1) .. (nextStart -1)
                    ))
                }

            }
        }
        return rt
    }


    fun mergeWith(other: TextElementResult): TextElementResult {
        return this.copy(
            cellRangeElements = cellRangeElements + other.cellRangeElements,
            others = others + other.others
        )
    }

    operator fun plus(other: TextElementResult): TextElementResult {
        return this.mergeWith(other)
    }

    operator fun plus(i: CellRangeElement): TextElementResult {
        return this.copy(
            cellRangeElements = cellRangeElements + i
        )
    }

    operator fun plus(i: OtherElement): TextElementResult {
        return this.copy(
            others = others + i
        )
    }
}
