package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.Func
import com.qxdzbc.p6.translator.formula.execution_unit.IntNum.Companion.exUnit
import com.qxdzbc.p6.translator.formula.execution_unit.RangeAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.StrUnit.Companion.exUnit
import org.mockito.kotlin.mock
import kotlin.test.*

internal class ExUnit_Func {

    @Test
    fun toFormula() {
        val u = Func(
            funcName = "qwe",
            args = listOf(
                1.exUnit(),
                "QWE".exUnit(),
                RangeAddressUnit(RangeAddress("B2:K9"))
            ),
            functionMapSt = mock()
        )
        assertEquals("qwe(1, \"QWE\", B2:K9)",u.toFormula())
    }
}
