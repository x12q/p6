package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.BasicTextElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CellRangeElementTest {
    @Test
    fun `contain 1 line`() {
        val c = CellRangeElement(
            rangeAddress = BasicTextElement("asd", 3,10)
        )
        assertTrue(c.contains(5))
        assertFalse(c.contains(2))
        assertFalse(c.contains(12))
    }

    @Test
    fun composite(){
//        val c = CellRangeElement(
//            rangeAddress=()
//        )
    }
}
