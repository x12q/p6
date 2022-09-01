package com.qxdzbc.p6.app.document.cell.d

import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.StateUtils.toMs
import kotlin.test.*

class CellContentTest {

    @Test
    fun run() {
        val c = CellContentImp(
            cellValueMs = CellValue.Companion.from(123).toMs(),
            exUnit = ExUnit.FALSE
        )
        val c2 = c.reRun()
        assertEquals(CellValue.from(false), c2?.reRun()?.cellValueAfterRun)
    }
}
