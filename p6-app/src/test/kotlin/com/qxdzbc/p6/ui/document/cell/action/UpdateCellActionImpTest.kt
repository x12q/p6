package com.qxdzbc.p6.ui.document.cell.action

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.ColdInit
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.ErrorDisplayText
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class UpdateCellActionImpTest {
    lateinit var ts: TestSample
    lateinit var act: UpdateCellAction
    val sc get() = ts.sc

    @BeforeTest
    fun b() {
        ts = TestSample()
        act = ts.comp.updateCellAction()
    }

    @Test
    fun `bug - A1+1 in A1`() {
        val wbws = WbWs(ts.wbKey1, ts.wsn1)
        ColdInit()
//        val q= ExUnitErrors()
//        ExUnitErrors.IncompatibleType.report("asd")
//        val q = ts.p6Comp.exUnitErrors()
        var ct = 0
        for(x in 0 .. 100){
            try{
                act.updateCellDM(
                    request = CellUpdateRequestDM(
                        cellId = CellIdDM(CellAddress("A1"), wbws),
                        cellContent = CellContentDM.fromFormula("=A1+1")
                    ),
                    publishError = false
                )
            }catch(e:Throwable){
                ct++
            }
        }
        assertEquals(0,ct)

    }

    @Test
    fun updateCell2() {
        // x: precondition
        val wbk = ts.wbKey1
        val wsn = ts.wsn1
        val sc by ts.stateContMs
        val ca = CellAddress("K2")
        fun gc() = sc.getCellOrDefault(wbk, wsn, ca)
        val c = gc()
        assertNotNull(c)
        assertEquals(null, c.currentValue)
        act.updateCellDM(
            CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = wbk, wsName = wsn, address = ca
                ),
                cellContent = CellContentDM(
                    cellValue = CellValue.Companion.from(123)
                )
            )
        )
        val c2 = gc()
        assertNotNull(c2)
        assertEquals(123.0, c2.currentValue)
    }

    @Test
    fun `bug - updateCell circular reference`() {
        val wbws = WbWs(ts.wbKey1, ts.wsn1)
        act.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = CellIdDM(CellAddress("A1"), wbws),
                cellContent = CellContentDM.fromFormula("=B1")
            ),
            publishError = false
        )
        act.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = CellIdDM(CellAddress("B1"), wbws),
                cellContent = CellContentDM.fromFormula("=A1")
            ),
            publishError = false
        )
        val b1 = sc.getCellOrDefault(wbws, CellAddress("B1"))!!
        val a1 = sc.getCellOrDefault(wbws, CellAddress("A1"))!!
        assertEquals(b1, a1.currentCellValue.cell)
        assertEquals(a1, b1.currentCellValue.cell)
        assertEquals(ErrorDisplayText.err, a1.cachedDisplayText)
        assertEquals(ErrorDisplayText.err, b1.cachedDisplayText)
    }
}


