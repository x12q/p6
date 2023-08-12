package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.function.FunctionExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.RangeAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.StrUnit.Companion.toExUnit
import org.mockito.kotlin.mock
import kotlin.test.*

internal class ExUnit_FuncUnit {

    @Test
    fun toFormula() {
        val u = FunctionExUnit(
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
