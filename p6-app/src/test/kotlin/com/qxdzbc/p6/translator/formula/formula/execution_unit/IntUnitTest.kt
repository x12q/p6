package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.translator.formula.execution_unit.IntUnit
import kotlin.test.*

class IntUnitTest {
    val u = IntUnit(100)
    val c1 = CellAddress(1, 1)
    val c2 = CellAddress(3, 3)
    @Test
    fun run() {
        assertEquals(Ok(100), u.runRs())
    }

    @Test
    fun shift() {
        assertEquals(u, u.shift(c1, c2))
    }

    @Test
    fun toFormula() {
        assertEquals(u.v.toString(), u.toFormula())
    }
}

