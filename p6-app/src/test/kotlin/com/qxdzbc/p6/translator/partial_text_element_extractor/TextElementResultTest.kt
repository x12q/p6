package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.BasicTextElement
import kotlin.test.*

internal class TextElementResultTest{
    @Test
    fun allWithPadding(){
        val t= TextElementResult(
            cellRangeElements = listOf(CellRangeElement(
                cellRangeLabel = "A1",
                start = 0,
                stop =  1
            )),
            basicTexts =  listOf(
                BasicTextElement(
                    "123",5 .. 8
                )
            )
        )
        val all = t.allSortedWithPadding()
        val expect = t.cellRangeElements + listOf(
            BasicTextElement(" ".repeat(3),2 .. 4)
        ) + t.basicTexts
        assertEquals(
            expect,all
        )
    }
}
