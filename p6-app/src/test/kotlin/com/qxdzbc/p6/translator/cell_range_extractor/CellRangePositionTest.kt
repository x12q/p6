package com.qxdzbc.p6.translator.cell_range_extractor

import kotlin.test.*

internal class CellRangePositionTest{
    @Test
    fun `contain 1 line`(){
        val c = CellRangePosition(
            cellRangeLabel="asd",
            start = TokenPosition(
                charIndex = 3
            ),
            stop = TokenPosition(
                charIndex = 10
            )
        )
        assertTrue(c.contains(5))
        assertFalse(c.contains(2))
        assertFalse(c.contains(12))
    }
}
