package com.qxdzbc.p6.app.action.cell.multi_cell_update

import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import test.TestSample
import kotlin.test.*

internal class UpdateMultiCellActionImpTest {
    lateinit var ts: TestSample
    lateinit var act:UpdateMultiCellAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act = ts.comp.multiCellUpdateAction()
    }
    @Test
    fun updateMultiCell() {
        val wbk = ts.wbKey1
        val wsn = ts.wsn1
        val request = UpdateMultiCellRequestDM(
            wsId = WorksheetIdDM(wbk, wsn),
            cellUpdateList = listOf(
                IndependentCellDM(
                    address = CellAddress("Q6"),
                    content = CellContentDM(formula = "=1+2+3", originalText = "=1+2+3")
                ),
                IndependentCellDM(
                    address = CellAddress("Q9"),
                    content = CellContentDM(formula = "=Q6+1", originalText = "=Q6+1")
                ),
                IndependentCellDM(
                    address = CellAddress("X4"),
                    content = CellContentDM(CellValue.fromAny(123), originalText = "123")
                )
            )
        )
        val sc = ts.sc
        val wsState = sc.getWsState(request)
        assertNotNull(wsState)

        // x: precondition
        assertTrue(wsState.cellStateCont.isEmpty())
        assertTrue(wsState.worksheet.isEmpty())

        // x: action
        val rs = act.updateMultiCellDM(request)

        // x: post condition
        assertTrue(rs is Ok)

        assertEquals(request.cellUpdateList.size, wsState.worksheet.size)
        assertEquals(request.cellUpdateList.size, wsState.cellStateCont.allElements.size)
        // Q6
        val q6 = wsState.getCellState("Q6")
        assertEquals("=1 + 2 + 3", q6?.cell?.fullFormulaFromExUnit)
        assertEquals(6.0, q6?.cell?.currentValue)

        // Q9
        val q9 = wsState.getCellState("Q9")
        assertEquals("=Q6 + 1", q9?.cell?.shortFormulaFromExUnit)
        assertEquals(7.0, (q9?.cell?.currentValue))

        // X4
        val x4 = wsState.getCellState("X4")
        assertEquals(123.0, (x4?.cell?.currentValue))
    }
}
