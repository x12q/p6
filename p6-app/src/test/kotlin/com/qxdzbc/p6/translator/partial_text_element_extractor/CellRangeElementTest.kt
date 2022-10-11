package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TokenPosition
import kotlin.test.*

internal class CellRangeElementTest{
    @Test
    fun `contain 1 line`(){
        val c = CellRangeElement(
            cellRangeLabel="asd",
            startTP = TokenPosition(
                charIndex = 3
            ),
            stopTP = TokenPosition(
                charIndex = 10
            )
        )
        assertTrue(c.contains(5))
        assertFalse(c.contains(2))
        assertFalse(c.contains(12))
    }
}
