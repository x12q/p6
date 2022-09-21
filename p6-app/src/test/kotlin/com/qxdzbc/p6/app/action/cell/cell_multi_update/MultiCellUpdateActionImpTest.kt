package com.qxdzbc.p6.app.action.cell.cell_multi_update

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import test.TestSample
import kotlin.test.*

internal class MultiCellUpdateActionImpTest {
    lateinit var ts: TestSample
    lateinit var act:MultiCellUpdateAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act = ts.p6Comp.multiCellUpdateAction()
    }
    @Test
    fun updateMultiCell() {
        val wbk = ts.wbKey1
        val wsn = ts.wsn1
        val request = MultiCellUpdateRequestDM(
            wsId = WorksheetIdDM(wbk,wsn),
            cellUpdateList = listOf(
                CellUpdateEntry(
                    cellAddress = CellAddress("Q6"),
                    contentDm = CellContentDM(formula="=1+2+3")
                ),
                CellUpdateEntry(
                    cellAddress = CellAddress("Q9"),
                    contentDm = CellContentDM(formula="=Q6+1")
                ),
                CellUpdateEntry(
                    cellAddress = CellAddress("X4"),
                    contentDm = CellContentDM(CellValue.fromAny(123))
                )
            )
        )
        val sc by ts.stateContMs
        val wsStateMs = sc.getWsStateMs(request)
        assertNotNull(wsStateMs)
        val wsState by wsStateMs

        // x: precondition
        assertTrue(wsState.cellStateCont.isEmpty())
        assertTrue(wsState.worksheet.isEmpty())

        // x: action
        val rs = act.updateMultiCell(request)

        // x: post condition
        assertTrue(rs is Ok)

        assertEquals(request.cellUpdateList.size,wsState.worksheet.size)
        assertEquals(request.cellUpdateList.size,wsState.cellStateCont.allElements.size)
        // Q6
        val q6=wsState.getCellState("Q6")
        assertEquals("=1 + 2 + 3",q6?.cell?.fullFormula)
        assertEquals(6.0,q6?.cell?.currentValue)

        // Q9
        val q9 = wsState.getCellState("Q9")
        assertEquals("=Q6 + 1",q9?.cell?.shortFormula)
        assertEquals(7.0,(q9?.cell?.currentValue))

        // X4
        val x4 = wsState.getCellState("X4")
        assertEquals(123.0,(x4?.cell?.currentValue))
    }
}
