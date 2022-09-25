package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.translator.formula.execution_unit.BoolUnit
import com.qxdzbc.p6.translator.formula.execution_unit.DoubleUnit
import com.qxdzbc.p6.translator.formula.execution_unit.IntUnit
import com.qxdzbc.p6.translator.formula.execution_unit.StrUnit
import kotlin.test.*

class PrimitiveUnitTest {
    @Test
    fun toFormula() {
        assertEquals("TRUE", BoolUnit.TRUE.toFormula())
        assertEquals("FALSE", BoolUnit.FALSE.toFormula())
        assertEquals("1", IntUnit(1).toFormula())
        assertEquals(1.23.toString(), DoubleUnit(1.23).toFormula())
        assertEquals("\"abc\"", StrUnit("abc").toFormula())
    }

    @Test
    fun shift() {
        val c1 = CellAddress("A1")
        val c2 = CellAddress("Q9")
        assertEquals(BoolUnit.TRUE, BoolUnit.TRUE.shift(c1, c2))
        assertEquals(BoolUnit.FALSE, BoolUnit.FALSE.shift(c1, c2))
        assertEquals(IntUnit(1), IntUnit(1).shift(c1, c2))
        assertEquals(DoubleUnit(1.23), DoubleUnit(1.23).shift(c1, c2))
        assertEquals(StrUnit("abc"), StrUnit("abc").shift(c1, c2))
    }
}

