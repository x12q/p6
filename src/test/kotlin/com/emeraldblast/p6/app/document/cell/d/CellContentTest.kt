package com.emeraldblast.p6.app.document.cell.d

import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import kotlin.test.*

class CellContentTest {

    @Test
    fun run() {
        val c = CellContentImp(
            cellValueMs = CellValue.Companion.from(123).toMs(),
            formula = "=FALSE",
            exUnit = ExUnit.FALSE
        )
        val c2 = c.reRun()
        assertEquals(CellValue.from(false), c2?.reRun()?.cellValueAfterRun)
    }
}
