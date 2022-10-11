package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.OtherElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TokenPosition
import kotlin.test.*

internal class TextElementResultTest{
    @Test
    fun allWithPadding(){
        val t= TextElementResult(
            cellRangeElements = listOf(CellRangeElement(
                cellRangeLabel = "A1",
                startTP = TokenPosition(0),
                stopTP =  TokenPosition(1)
            )),
            others =  listOf(
                OtherElement(
                    "123",5 .. 8
                )
            )
        )
        val all = t.allSortedWithPadding()
        val expect = t.cellRangeElements + listOf(
            OtherElement(" ".repeat(3),2 .. 4)
        ) + t.others
        assertEquals(
            expect,all
        )
    }
}
