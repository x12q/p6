package com.qxdzbc.p6.document_data_layer.cell

import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.BoolUnit.Companion.FALSE
import kotlin.test.*

class CellContentTest {

    @Test
    fun run() {
        val c = CellContentImp(
            cellValueMs = CellValue.from(123).toMs(),
            exUnit = FALSE,
            originalText ="=FALSE"
        )
        val c2 = c.reRunRs().component1()
        assertEquals(CellValue.from(false), c2?.reRunRs()?.component1()?.cellValueAfterRun)
    }
}
