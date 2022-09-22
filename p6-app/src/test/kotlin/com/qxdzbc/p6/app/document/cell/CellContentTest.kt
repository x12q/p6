package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue
import kotlin.test.*

class CellContentTest {

    @Test
    fun run() {
        val c = CellContentImp(
            cellValueMs = CellValue.from(123).toMs(),
            exUnit = ExUnit.FALSE
        )
        val c2 = c.reRun()
        assertEquals(CellValue.from(false), c2?.reRun()?.cellValueAfterRun)
    }
}
