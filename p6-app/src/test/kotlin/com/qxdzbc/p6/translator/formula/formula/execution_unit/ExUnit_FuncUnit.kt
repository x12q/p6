package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.FuncUnit
import com.qxdzbc.p6.translator.formula.execution_unit.IntUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.RangeAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.StrUnit.Companion.toExUnit
import org.mockito.kotlin.mock
import kotlin.test.*

internal class ExUnit_FuncUnit {

    @Test
    fun toFormula() {
        val u = FuncUnit(
            funcName = "qwe",
            args = listOf(
                1.toExUnit(),
                "QWE".toExUnit(),
                RangeAddressUnit(RangeAddress("B2:K9"))
            ),
            functionMapSt = mock()
        )
        assertEquals("qwe(1, \"QWE\", B2:K9)",u.toFormula())
    }
}
