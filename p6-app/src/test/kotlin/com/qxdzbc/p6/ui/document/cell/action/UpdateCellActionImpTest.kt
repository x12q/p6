package com.qxdzbc.p6.ui.document.cell.action

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.rpc.cell.msg.CellContentProtoDM
import test.TestSample
import kotlin.test.*

internal class UpdateCellActionImpTest {
    lateinit var ts: TestSample
    lateinit var act:UpdateCellAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act = ts.p6Comp.updateCellAction()
    }
    @Test
    fun updateCell2() {
        // x: precondition
        val wbk = ts.wbKey1
        val wsn = ts.wsn1
        val sc by ts.stateContMs
        val ca = CellAddress("K2")
        fun gc() = sc.getCell(wbk,wsn,ca )
        val c= gc()
        assertNotNull(c)
        assertEquals(null,c.currentValue)
        act.updateCell2(
            CellUpdateRequest2(
                wbKey = wbk,wsName =wsn, cellAddress = ca, cellContent = CellContentProtoDM(
                    cellValue = CellValue.Companion.from(123)
                )
            )
        )
        val c2 = gc()
        assertNotNull(c2)
        assertEquals(123.0,c2.currentValue)
    }
}


